
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
                }
            });
            thread.start();
            threads.add(thread);
        }

        for (Thread thr : threads) {
            thr.join();
        }

        Map.Entry<Integer, Integer> maxMap = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();

        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxMap.getKey(), maxMap.getValue());
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> map : sizeToFreq.entrySet()) {
            if (map.getKey() != maxMap.getKey()) {
                System.out.printf("- %d (%d раз)\n", map.getKey(), map.getValue());
            }
        }
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
