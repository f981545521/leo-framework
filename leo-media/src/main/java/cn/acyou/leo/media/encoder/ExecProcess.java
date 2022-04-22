package cn.acyou.leo.media.encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;
import ws.schild.jave.info.VideoSize;
import ws.schild.jave.utils.RBufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author youfang
 * @version [1.0.0, 2022/4/22 13:45]
 **/
public abstract class ExecProcess {

    private static final Logger LOG = LoggerFactory.getLogger(ExecProcess.class);

    private long inputDuration = 0;

    private MultimediaInfo info = null;

    private boolean callInfo = false;

    public void handlerOutPut(String input, InputStream errorStream) {
        parseMultimediaInfo(input, new RBufferedReader(new InputStreamReader(errorStream)));
    }

    private static final Pattern SIZE_PATTERN = Pattern.compile("(\\d+)x(\\d+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern FRAME_RATE_PATTERN = Pattern.compile("([\\d.]+)\\s+(?:fps|tbr)", Pattern.CASE_INSENSITIVE);
    private static final Pattern BIT_RATE_PATTERN = Pattern.compile("(\\d+)\\s+kb/s", Pattern.CASE_INSENSITIVE);
    private static final Pattern SAMPLING_RATE_PATTERN = Pattern.compile("(\\d+)\\s+Hz", Pattern.CASE_INSENSITIVE);
    private static final Pattern CHANNELS_PATTERN = Pattern.compile("(mono|stereo|quad)", Pattern.CASE_INSENSITIVE);

    private void parseMultimediaInfo(String source, RBufferedReader reader) {
        Pattern p1 = Pattern.compile("^\\s*Input #0, (\\w+).+$\\s*", Pattern.CASE_INSENSITIVE);
        Pattern p21 = Pattern.compile("^\\s*Duration:.*$", Pattern.CASE_INSENSITIVE);
        Pattern p22 = Pattern.compile("^\\s*Duration: (\\d\\d):(\\d\\d):(\\d\\d)\\.(\\d\\d).*$", Pattern.CASE_INSENSITIVE);
        Pattern p3 = Pattern.compile("^\\s*Stream #\\S+: ((?:Audio)|(?:Video)|(?:Data)): (.*)\\s*$", Pattern.CASE_INSENSITIVE);
        Pattern p4 = Pattern.compile("^\\s*Metadata:", Pattern.CASE_INSENSITIVE);
        Pattern p5 = Pattern.compile("^\\s*(\\w+)\\s*:\\s*(\\S+)\\s*$", Pattern.CASE_INSENSITIVE);
        try {
            int step = 0;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                //LOG.info(line);
                switch (step) {
                    case 0: {
                        String token = source + ": ";
                        if (line.startsWith(token)) {
                            String message = line.substring(token.length());
                            throw new RuntimeException(message);
                        }
                        Matcher m = p1.matcher(line);
                        if (m.matches()) {
                            String format = m.group(1);
                            info = new MultimediaInfo();
                            info.setFormat(format);
                            step++;
                        }
                        break;
                    }
                    case 1: {
                        Matcher m1 = p21.matcher(line);
                        Matcher m2 = p22.matcher(line);
                        if (m1.matches()) {
                            if (m2.matches()) {
                                long hours = Integer.parseInt(m2.group(1));
                                long minutes = Integer.parseInt(m2.group(2));
                                long seconds = Integer.parseInt(m2.group(3));
                                long dec = Integer.parseInt(m2.group(4));
                                long duration =
                                        (dec * 10L)
                                                + (seconds * 1000L)
                                                + (minutes * 60L * 1000L)
                                                + (hours * 60L * 60L * 1000L);
                                info.setDuration(duration);
                                this.inputDuration = duration;
                                step++;
                            } else {
                                step++;
                                // step = 3;
                            }
                        } else {
                            Matcher m4 = p4.matcher(line);
                            if (m4.matches()) {
                                line = reader.readLine();
                                while (line != null && !p21.matcher(line).matches()) {
                                    Matcher m5 = p5.matcher(line);
                                    if (m5.matches()) {
                                        info.getMetadata().put(m5.group(1), m5.group(2));
                                    }
                                    line = reader.readLine();
                                }
                                reader.reinsertLine(line);
                            }
                        }
                        break;
                    }
                    case 2: {
                        Matcher m = p3.matcher(line);
                        if (m.matches()) {
                            String type = m.group(1);
                            String specs = m.group(2);
                            if ("Video".equalsIgnoreCase(type)) {
                                VideoInfo video = new VideoInfo();
                                StringTokenizer st = new StringTokenizer(specs, ",");
                                for (int i = 0; st.hasMoreTokens(); i++) {
                                    String token = st.nextToken().trim();
                                    if (i == 0) {
                                        video.setDecoder(token);
                                    } else {
                                        boolean parsed = false;
                                        // Video size.
                                        Matcher m2 = SIZE_PATTERN.matcher(token);
                                        if (!parsed && m2.find()) {
                                            int width = Integer.parseInt(m2.group(1));
                                            int height = Integer.parseInt(m2.group(2));
                                            video.setSize(new VideoSize(width, height));
                                            parsed = true;
                                        }
                                        // Frame rate.
                                        m2 = FRAME_RATE_PATTERN.matcher(token);
                                        if (!parsed && m2.find()) {
                                            try {
                                                float frameRate = Float.parseFloat(m2.group(1));
                                                video.setFrameRate(frameRate);
                                            } catch (NumberFormatException e) {
                                                LOG.info("Invalid frame rate value: " + m2.group(1), e);
                                            }
                                            parsed = true;
                                        }
                                        // Bit rate.
                                        m2 = BIT_RATE_PATTERN.matcher(token);
                                        if (!parsed && m2.find()) {
                                            int bitRate = Integer.parseInt(m2.group(1));
                                            video.setBitRate(bitRate * 1000);
                                            parsed = true;
                                        }
                                    }
                                }
                                //reading vedio metadata
                                line = reader.readLine();
                                Matcher m4 = p4.matcher(line);
                                if (m4.matches()) {
                                    line = reader.readLine();
                                    while (line != null && p5.matcher(line).matches()) {
                                        Matcher m5 = p5.matcher(line);
                                        if (m5.matches()) {
                                            video.getMetadata().put(m5.group(1), m5.group(2));
                                        }
                                        line = reader.readLine();
                                    }
                                    reader.reinsertLine(line);
                                } else {
                                    reader.reinsertLine(line);
                                }
                                info.setVideo(video);
                            } else if ("Audio".equalsIgnoreCase(type)) {
                                AudioInfo audio = new AudioInfo();
                                StringTokenizer st = new StringTokenizer(specs, ",");
                                for (int i = 0; st.hasMoreTokens(); i++) {
                                    String token = st.nextToken().trim();
                                    if (i == 0) {
                                        audio.setDecoder(token);
                                    } else {
                                        boolean parsed = false;
                                        // Sampling rate.
                                        Matcher m2 = SAMPLING_RATE_PATTERN.matcher(token);
                                        if (!parsed && m2.find()) {
                                            int samplingRate = Integer.parseInt(m2.group(1));
                                            audio.setSamplingRate(samplingRate);
                                            parsed = true;
                                        }
                                        // Channels.
                                        m2 = CHANNELS_PATTERN.matcher(token);
                                        if (!parsed && m2.find()) {
                                            String ms = m2.group(1);
                                            if ("mono".equalsIgnoreCase(ms)) {
                                                audio.setChannels(1);
                                            } else if ("stereo".equalsIgnoreCase(ms)) {
                                                audio.setChannels(2);
                                            } else if ("quad".equalsIgnoreCase(ms)) {
                                                audio.setChannels(4);
                                            }
                                            parsed = true;
                                        }
                                        // Bit rate.
                                        m2 = BIT_RATE_PATTERN.matcher(token);
                                        if (!parsed && m2.find()) {
                                            int bitRate = Integer.parseInt(m2.group(1));
                                            audio.setBitRate(bitRate * 1000);
                                            parsed = true;
                                        }
                                    }
                                }
                                //reading audio metadata
                                line = reader.readLine();
                                Matcher m4 = p4.matcher(line);
                                if (m4.matches()) {
                                    line = reader.readLine();
                                    while (line != null && p5.matcher(line).matches()) {
                                        Matcher m5 = p5.matcher(line);
                                        if (m5.matches()) {
                                            audio.getMetadata().put(m5.group(1), m5.group(2));
                                        }
                                        line = reader.readLine();
                                    }
                                    reader.reinsertLine(line);
                                } else {
                                    reader.reinsertLine(line);
                                }
                                info.setAudio(audio);
                            }
                        } else // if (m4.matches())
                        {
                            // Stay on level 2
                        }
              /*
                 else
                 {
                 step = 3;
                 }
              */
                        break;
                    }
                    default:
                        break;
                }

                if (line.startsWith("frame=") || line.startsWith("size=")) {
                    if (!callInfo) {
                        mediaInfo(info);
                        callInfo = true;
                    }
                    try {
                        line = line.trim();
                        if (line.length() > 0) {
                            HashMap<String, String> table = parseProgressInfoLine(line);
                            if (table != null) {
                                String time = table.get("time");
                                if (time != null) {
                                    String dParts[] = time.split(":");
                                    // HH:MM:SS.xx
                                    Double seconds = Double.parseDouble(dParts[dParts.length - 1]);
                                    if (dParts.length > 1) {
                                        seconds += Double.parseDouble(dParts[dParts.length - 2]) * 60;
                                        if (dParts.length > 2) {
                                            seconds += Double.parseDouble(dParts[dParts.length - 3]) * 60 * 60;
                                        }
                                    }

                                    int perm = (int) Math.round((seconds * 1000L * 1000L) / (double) inputDuration);
                                    if (perm > 1000) {
                                        perm = 1000;
                                    }
                                    progress(perm);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        LOG.warn("Error in progress parsing for line: {}", line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (info == null) {
            throw new RuntimeException();
        }
    }

    public void mediaInfo(MultimediaInfo multimediaInfo) {

    }

    ;

    public void progress(long perm) {

    }

    ;

    private static final Pattern PROGRESS_INFO_PATTERN = Pattern.compile("\\s*(\\w+)\\s*=\\s*(\\S+)\\s*", Pattern.CASE_INSENSITIVE);

    private HashMap<String, String> parseProgressInfoLine(String line) {
        HashMap<String, String> table = null;
        Matcher m = PROGRESS_INFO_PATTERN.matcher(line);
        while (m.find()) {
            if (table == null) {
                table = new HashMap<>();
            }
            String key = m.group(1);
            String value = m.group(2);
            table.put(key, value);
        }
        return table;
    }


}
