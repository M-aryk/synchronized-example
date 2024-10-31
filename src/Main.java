import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {

                int times;
                String text = generateRoute("RLRFR", 100);
                int count = (int) text.chars().filter(ch -> ch == 'R').count();
                System.out.println(text + " " + count);
                if (sizeToFreq.containsKey(count)) {
                    times = sizeToFreq.get(count) + 1;
                } else {
                    times = 1;
                }
                synchronized (sizeToFreq) {
                    sizeToFreq.put(count, times);
                    sizeToFreq.notify();
                }
            });
            thread.start();
            threads.add(thread);
        }

        Thread thread1 = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    maxRepeats();
                }
            }
        });
        thread1.start();

        for (Thread thr : threads) {
            thr.join();
        }

        thread1.interrupt();

        Map.Entry<Integer, Integer> maxMap = maxRepeats();
        otherRepeats(maxMap);
    }

    private static void otherRepeats(Map.Entry<Integer, Integer> maxMap) {
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> map : sizeToFreq.entrySet()) {
            if (map.getKey() != maxMap.getKey()) {
                System.out.printf("- %d (%d раз)\n", map.getKey(), map.getValue());
            }
        }
    }

    private static Map.Entry<Integer, Integer> maxRepeats() {
        Map.Entry<Integer, Integer> maxMap = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();

        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxMap.getKey(), maxMap.getValue());
        return maxMap;
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}