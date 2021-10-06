package org.djna.asynch.estate.emulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.djna.asynch.estate.data.*;

import javax.jms.*;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

// Emulates Telemetry from multiple devices.
// Starts a thread per emualted device, each publishing to a separate topic
// and each publishing at a specified rate. Cycles through a range of values.
public class TelemetryEmulator {
    private static final Logger LOGGER = Logger.getLogger(TelemetryEmulator.class);
    private static final String baseTopicNormal = "home.thermostats";
    private static final String baseTopicWarn = "home.thermostats.warn";
    private static String baseTopic = "home.thermostats";

    public static void main(String[] args) throws Exception {
        // verify that logging is correctly configured in logback.xml
        LOGGER.error("Test Error");
        LOGGER.info("Starting");
        // usually don't enable to see this, debug from libraries is versbose
        LOGGER.debug("debug message");


        // house numbers from 101 to 145
        // each house has a thermostat for living room, bedroom and kitchen
//        for (int houseNum = 101; houseNum <= 145; houseNum++) {
//            startWork(makeDevice(""+ houseNum,"living", 10), false);
//            startWork(makeDevice(""+ houseNum,"bedroom", 10), false);
//            startWork(makeDevice(""+ houseNum,"kitchen", 10), false);
//        }
        startWork(makeDevice(""+ 101,"living", 10), false);
        startWork(makeDevice(""+ 101,"bedroom", 10), false);
        startWork(makeDevice(""+ 101,"kitchen", 10), false);
    }

    // starts thread for specified emulator
    public static void startWork(Runnable deviceEmulator, boolean daemon) {
        Thread deviceThread = new Thread(deviceEmulator);
        deviceThread.setDaemon(daemon);
        deviceThread.start();
    }

    // Device factory
    public static Runnable makeDevice(String property, String location, final int frequencySeconds) {
        return new Runnable() {
            // each device establishes its own connection
            // as an enhancement we could start and stop them indepdently
            private ActiveMQConnectionFactory connectionFactory;
            private Connection connection;
            private Session session;
            private Destination destination;
            private MessageProducer producer;

            // currently we just run forever, but this is our shutdown flag
            private boolean stopping = false;

            @Override
            public void run() {
                try {
                    connectionFactory
                            = new ActiveMQConnectionFactory("tcp://localhost:61616");
                    Connection connection = connectionFactory.createConnection();
                    connection.start();
                    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                    // in ActiceMQ this will create a topic if it doesn't exist
                    String topic = MessageFormat.format(
                            "{0}.{1}.{2}", baseTopic, property, location);
                    destination = session.createTopic(topic);

                    // Create a MessageProducer from the Session to the Topic or Queue
                    producer = session.createProducer(destination);
                    // TODO - set QOS options here

                    Random r = new Random();

                    int baseTemperature = r.nextInt(20) + 10;
                    int temperatureSkew = r.nextInt(5) - 10;

                    LatestThreeTempBelowFive readingChecker = new LatestThreeTempBelowFive();

                    // TODO - add capability for clean shutdown
                    while (!stopping) {
                        readingChecker.add(baseTemperature + temperatureSkew);
//                        System.out.println(readingChecker.listOfTemps);
                        boolean sendWarning =  readingChecker.sendWarning();
                        if (sendWarning) {
//                            System.out.println("WARN!!!!!");
                            // make new topic
                            baseTopic = baseTopicWarn;
                            topic = MessageFormat.format(
                                    "{0}.{1}.{2}", baseTopic, property, location);
                            destination = session.createTopic(topic);

                            // Create a MessageProducer from the Session to the Topic or Queue
                            producer = session.createProducer(destination);
                            publishTemperature(baseTemperature + temperatureSkew, location);
                            // end of make new topic

                            // revert back

                            baseTopic = baseTopicNormal;
                            topic = MessageFormat.format(
                                    "{0}.{1}.{2}", baseTopic, property, location);
                            destination = session.createTopic(topic);

                            // Create a MessageProducer from the Session to the Topic or Queue
                            producer = session.createProducer(destination);
                            // end of revert back
                        }
                        publishTemperature(baseTemperature + temperatureSkew, location );
                        // prepare next values
                        temperatureSkew++;
                        temperatureSkew = r.nextInt(5) - 10;

                        // good citizen check
                        int sleepFor =  frequencySeconds < 15 ? 15 : frequencySeconds;
                        TimeUnit.SECONDS.sleep(sleepFor);
                    }

                    session.close();
                    connection.close();
                } catch (Exception e) {
                    System.out.println("Caught: " + e);
                    e.printStackTrace();
                }
            }

            private void publishTemperature( int temperature, String location ) throws JMSException, JsonProcessingException {

                Date todayDate = Calendar.getInstance().getTime();
//                String location = "sample";
                ThermostatReading reading = new ThermostatReading(todayDate, temperature, location);

                // publish JSON from reading
                ObjectMapper mapper = new ObjectMapper();
                String text = mapper.writeValueAsString(reading);


//                String text = "{\"date\":1633362327823,\"temperature\":"
//                        + temperature
//                        + ",\"location\":\"hall\"}";
                TextMessage message = session.createTextMessage(text);

                System.out.println("Sent message to "
                        + destination + ":"
                        + message.getText() + " : "
                        + Thread.currentThread().getName());
                producer.send(message);
            }
        };
    }
}