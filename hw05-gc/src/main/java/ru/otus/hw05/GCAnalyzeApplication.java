package ru.otus.hw05;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class GCAnalyzeApplication {

    private static long                                                    workedTime;
    private        CopyOnWriteArrayList<GarbageCollectionNotificationInfo> notificationInfos = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws Exception {
        GCAnalyzeApplication gcAnalyzeApplication = new GCAnalyzeApplication();

        gcAnalyzeApplication.switchOnMonitoring();

        MemoryLeakProcess leakProcess = new MemoryLeakProcess();

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=MemoryLeakProcess");

        mbs.registerMBean(leakProcess, name);

        long startTime = System.currentTimeMillis();
        try {
            leakProcess.run();
        } catch (OutOfMemoryError outOfMemoryError) {
            workedTime = (System.currentTimeMillis() - startTime);
            System.out.println("Memory overload. Worked time, sec: " + workedTime/1000);
        }

        gcAnalyzeApplication.printReport(leakProcess.getSize());
    }


    private void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    notificationInfos.add(info);

                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private void printReport(int size) {

        Map<String, List<GarbageCollectionNotificationInfo>> infoByGcName
                = notificationInfos.stream().collect(Collectors.groupingBy(GarbageCollectionNotificationInfo::getGcName));

        LongSummaryStatistics summaryStatistics = notificationInfos.stream().collect(Collectors.summarizingLong(value -> value.getGcInfo().getDuration()));

        System.out.println("Report:");
        System.out.println("All Duration (ms): " + summaryStatistics.getSum() + "; Min duration: " + summaryStatistics.getMin() + "; Max duration: " + summaryStatistics.getMax());

        if (workedTime != 0)
            System.out.println("All duration / Worked Time: " + (summaryStatistics.getSum() / (float) workedTime));

        System.out.println("Objects count: " + size + "\n");
        // By gcName
        infoByGcName.forEach((gcName, nameInfos) -> {
                                 System.out.println(gcName + ":\n");
                                 Map<Long, LongSummaryStatistics> minStatMap = nameInfos.stream().collect(Collectors.groupingBy(
                                         ni -> ni.getGcInfo().getStartTime() / 1000 / 60,
                                         Collectors.summarizingLong(value -> value.getGcInfo().getDuration())));

                                 // By minutes statistics
                                 minStatMap.forEach((min, durationSummary) ->
                                                            System.out.println(
                                                                    "min: " + (min + 1) + "; "
                                                                            + "Summary duration (ms):" + durationSummary.getSum()
                                                                            + "; Avg duration (ms): " + durationSummary.getSum() / durationSummary.getCount()
                                                                            + "; gc run count (per min):" + durationSummary.getCount()));
                                 System.out.println("___________________________\n");
                             }
                            );
    }
}
