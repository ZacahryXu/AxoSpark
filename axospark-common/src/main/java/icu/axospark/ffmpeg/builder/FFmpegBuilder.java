package icu.axospark.ffmpeg.builder;

import icu.axospark.ffmpeg.FFmpeg;
import icu.axospark.ffmpeg.pojo.FFmpegResult;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * FFmpeg构造器
 */
public class FFmpegBuilder {
   private final FFmpeg ffmpeg;
   private String inputFile;
   private String outputFile;
   private String videoCodec = "libx264";
   private String audioCodec = "aac";
   private String resolution; // 例如: "1920x1080"
   private String videoBitrate; // 例如: "2M"
   private String audioBitrate; // 例如: "128k"
   private Integer fps;
   private boolean overwrite = true;
   private final List<String> customArgs = new ArrayList<>();

   public FFmpegBuilder(FFmpeg ffmpeg) {
      this.ffmpeg = ffmpeg;
   }

   public FFmpegBuilder input(String inputFile) {
      this.inputFile = inputFile;
      return this;
   }

   public FFmpegBuilder output(String outputFile) {
      this.outputFile = outputFile;
      return this;
   }

   public FFmpegBuilder videoCodec(String codec) {
      this.videoCodec = codec;
      return this;
   }

   public FFmpegBuilder audioCodec(String codec) {
      this.audioCodec = codec;
      return this;
   }

   public FFmpegBuilder resolution(String resolution) {
      this.resolution = resolution;
      return this;
   }

   public FFmpegBuilder videoBitrate(String bitrate) {
      this.videoBitrate = bitrate;
      return this;
   }

   public FFmpegBuilder audioBitrate(String bitrate) {
      this.audioBitrate = bitrate;
      return this;
   }

   public FFmpegBuilder fps(int fps) {
      this.fps = fps;
      return this;
   }

   public FFmpegBuilder overwrite(boolean overwrite) {
      this.overwrite = overwrite;
      return this;
   }

   public FFmpegBuilder addCustomArg(String arg) {
      this.customArgs.add(arg);
      return this;
   }

   public List<String> build() {
      validate();

      List<String> cmd = new ArrayList<>();
      cmd.add(ffmpeg.ffmpegPath);

      // 覆盖输出文件
      if (overwrite) {
         cmd.add("-y");
      }

      // 输入文件
      cmd.add("-i");
      cmd.add(inputFile);

      // 视频编码
      if (videoCodec != null) {
         cmd.add("-c:v");
         cmd.add(videoCodec);
      }

      // 音频编码
      if (audioCodec != null) {
         cmd.add("-c:a");
         cmd.add(audioCodec);
      }

      // 分辨率
      if (resolution != null) {
         cmd.add("-s");
         cmd.add(resolution);
      }

      // 视频码率
      if (videoBitrate != null) {
         cmd.add("-b:v");
         cmd.add(videoBitrate);
      }

      // 音频码率
      if (audioBitrate != null) {
         cmd.add("-b:a");
         cmd.add(audioBitrate);
      }

      // 帧率
      if (fps != null) {
         cmd.add("-r");
         cmd.add(String.valueOf(fps));
      }

      // 自定义参数
      cmd.addAll(customArgs);

      // 输出文件
      cmd.add(outputFile);

      return cmd;


   }
   private void validate() {
      if (inputFile == null || inputFile.trim().isEmpty()) {
         throw new IllegalArgumentException("输入文件不能为空");
      }
      if (outputFile == null || outputFile.trim().isEmpty()) {
         throw new IllegalArgumentException("输出文件不能为空");
      }
      if (!Files.exists(Paths.get(inputFile))) {
         throw new IllegalArgumentException("输入文件不存在: " + inputFile);
      }
   }




}
