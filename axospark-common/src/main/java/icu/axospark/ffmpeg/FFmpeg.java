package icu.axospark.ffmpeg;


import icu.axospark.ffmpeg.builder.FFmpegBuilder;
import icu.axospark.ffmpeg.pojo.FFmpegResult;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 初始化可执行ffmpeg
 */
@Slf4j
public class FFmpeg {
    public final String ffmpegPath;

    public FFmpeg(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
        validateFFmpegPath();
        log.info("✅ FFmpeg 初始化成功: {}", ffmpegPath);
    }

    /**
     * 使用默认路径（自动检测系统）
     */
    public FFmpeg() {
        this(detectFFmpegPath());
    }

    private void validateFFmpegPath() {
        if (!Files.exists(Paths.get(ffmpegPath))) {
            throw new IllegalArgumentException("FFmpeg 可执行文件不存在: " + ffmpegPath);
        }
    }

    private static String detectFFmpegPath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "./lib/ffmpeg/windows/ffmpeg.exe";
        } else if (os.contains("mac")) {
            return "./lib/ffmpeg/mac/ffmpeg";
        } else {
            return "/usr/bin/ffmpeg"; // Linux
        }
    }

    public FFmpegBuilder builder() {
        return new FFmpegBuilder(this);
    }
    /**
     * 执行ffmpeg命令
     */
    public FFmpegResult execute(List<String> cmd,long timeoutMinutes){
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectErrorStream(false);
        log.info("执行命令: {}",String.join(" ",cmd));
        return null;
    }
    private static class StreamGobbler extends Thread{
        private final InputStream inputStream;
        private final String type;
        private final StringBuilder output = new StringBuilder();
        StreamGobbler(InputStream inputStream,String type){
            this.inputStream = inputStream;
            this.type = type;
        }

        @Override
        public void run() {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
                String line;
                while((line = reader.readLine())!=null){
                    output.append(line).append("\n");
                    log.debug("[{}] {}",type,line);
                }
            }catch (IOException e){
                log.error("读取流失败",e);
            }
        }
        public String getOutput(){
            return output.toString();
        }
    }


}
