package test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 极简版雪花算法ID生成器
 * 专为单体项目设计，用于视频文件等唯一标识
 * 
 * 64位ID组成:
 * - 1位: 固定为0 (保证ID为正数)
 * - 41位: 时间戳 (毫秒级，可用69年)
 * - 22位: 序列号 (每毫秒419万个ID)
 * 
 * 特点:
 * 1. 无需配置，开箱即用
 * 2. 时间有序，方便按时间排序
 * 3. 单机每毫秒419万并发，性能充足
 * 4. 代码极简，易于理解和维护
 */
public class VideoIDGenerator {
    
    // 起始时间戳 (2024-01-01 00:00:00)
    private static final long EPOCH = 1760918400000L;
    
    // 序列号位数：22位
    private static final long SEQUENCE_BITS = 22L;
    
    // 序列号最大值：4194303 (约419万)
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    
    // 时间戳左移位数
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS;
    
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    
    /**
     * 构造函数 - 无需任何参数
     */
    public VideoIDGenerator() {
        System.out.println(String.format(
            "VideoIDGenerator初始化完成，每毫秒可生成 %,d 个ID",
            MAX_SEQUENCE + 1));
    }
    
    /**
     * 生成唯一ID
     * 
     * @return 64位唯一ID
     */
    public synchronized long generate() {
        long timestamp = currentTimeMillis();
        
        // 时钟回拨检测
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                String.format("时钟回拨，拒绝生成ID。回拨了 %d 毫秒", lastTimestamp - timestamp));
        }
        
        // 同一毫秒内
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号溢出，等待下一毫秒
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 新的毫秒，序列号重置为0
            sequence = 0L;
        }
        
        lastTimestamp = timestamp;
        
        // 组装ID: 时间戳(41位) | 序列号(22位)
        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT) | sequence;
    }
    
    /**
     * 等待下一毫秒
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimeMillis();
        }
        return timestamp;
    }
    
    /**
     * 获取当前时间戳(毫秒)
     */
    private long currentTimeMillis() {
        return System.currentTimeMillis();
    }
    
    /**
     * 解析ID，提取时间戳和序列号
     * 
     * @param id 64位ID
     * @return ID信息对象
     */
    public static IDInfo parse(long id) {
        long timestamp = ((id >> TIMESTAMP_SHIFT) + EPOCH);
        long sequence = id & MAX_SEQUENCE;
        
        return new IDInfo(id, timestamp, sequence);
    }
    
    /**
     * ID信息类
     */
    public static class IDInfo {
        private final long id;
        private final long timestamp;
        private final long sequence;
        
        public IDInfo(long id, long timestamp, long sequence) {
            this.id = id;
            this.timestamp = timestamp;
            this.sequence = sequence;
        }
        
        public long getId() { 
            return id; 
        }
        
        public long getTimestamp() { 
            return timestamp; 
        }
        
        public long getSequence() { 
            return sequence; 
        }
        
        public String getDateTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return sdf.format(new Date(timestamp));
        }
        
        @Override
        public String toString() {
            return String.format(
                "IDInfo{\n" +
                "  id=%d\n" +
                "  timestamp=%d\n" +
                "  datetime='%s'\n" +
                "  sequence=%d\n" +
                "}",
                id, timestamp, getDateTime(), sequence
            );
        }
    }
    
    /**
     * 测试示例
     */
    public static void main(String[] args) {
        VideoIDGenerator generator = new VideoIDGenerator();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("极简版雪花算法ID生成器演示");
        System.out.println("=".repeat(60));
        
        // 生成单个ID
        System.out.println("\n【生成视频ID】");
        long videoId = generator.generate();
        System.out.println("视频ID: " + videoId);
        System.out.println("文件名: " + videoId + ".mp4");
        
        // 解析ID
        System.out.println("\n" + "=".repeat(60));
        System.out.println("【解析ID信息】");
        System.out.println("=".repeat(60));
        IDInfo info = parse(videoId);
        System.out.println(info);
        
        // 批量生成
        System.out.println("\n" + "=".repeat(60));
        System.out.println("【批量生成10个ID】");
        System.out.println("=".repeat(60));
        for (int i = 0; i < 10; i++) {
            long id = generator.generate();
            System.out.printf("视频 #%-2d: %d.mp4%n", i + 1, id);
        }
        
        // 验证单调递增性
        System.out.println("\n" + "=".repeat(60));
        System.out.println("【验证ID单调递增性】");
        System.out.println("=".repeat(60));
        long prev = 0;
        boolean isMonotonic = true;
        for (int i = 0; i < 1000; i++) {
            long id = generator.generate();
            if (id <= prev) {
                isMonotonic = false;
                System.out.println("发现非单调: " + prev + " >= " + id);
                break;
            }
            prev = id;
        }
        System.out.println("单调递增性验证: " + (isMonotonic ? "✓ 通过" : "✗ 失败"));
        
        // 验证唯一性
        System.out.println("\n" + "=".repeat(60));
        System.out.println("【验证ID唯一性】");
        System.out.println("=".repeat(60));
        java.util.Set<Long> idSet = new java.util.HashSet<>();
        int testCount = 10000;
        for (int i = 0; i < testCount; i++) {
            long id = generator.generate();
            if (!idSet.add(id)) {
                System.out.println("发现重复ID: " + id);
                break;
            }
        }
        System.out.println(String.format(
            "生成 %,d 个ID，唯一性验证: %s", 
            testCount, 
            idSet.size() == testCount ? "✓ 通过" : "✗ 失败"
        ));
        
        // 性能测试
        System.out.println("\n" + "=".repeat(60));
        System.out.println("【性能测试 - 生成100,000个ID】");
        System.out.println("=".repeat(60));
        long start = System.currentTimeMillis();
        int count = 100000;
        for (int i = 0; i < count; i++) {
            generator.generate();
        }
        long end = System.currentTimeMillis();
        
        double duration = (end - start) / 1000.0;
        System.out.printf("总耗时: %.3f 秒%n", duration);
        System.out.printf("QPS: %,.0f 个/秒%n", count / duration);
        System.out.printf("平均耗时: %.3f 微秒/个%n", duration * 1000000 / count);
        
        // 模拟实际使用场景
        System.out.println("\n" + "=".repeat(60));
        System.out.println("【模拟MinIO视频上传场景】");
        System.out.println("=".repeat(60));
        for (int i = 0; i < 5; i++) {
            long id = generator.generate();
            String fileName = id + ".mp4";
            String thumbnailName = id + "_thumb.jpg";
            
            System.out.printf(
                "视频 %d:\n  原视频: %s\n  缩略图: %s\n  上传时间: %s\n\n",
                i + 1, fileName, thumbnailName, parse(id).getDateTime()
            );
        }
    }
}