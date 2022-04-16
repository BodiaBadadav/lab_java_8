import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Crawler {
    /** Объект для синхронизации работы потоков */
    private static final Object monitor = new Object();

    /** Блокирующая очередь */
    private static final BlockingQueue queue = new BlockingQueue(monitor);

    /** Максимальное количество работающих потоков */
    private static final int threadsCount = 500;

    /** Максимальная глубина поиска */
    private static final int maxDepth = 4;

    /** Лист для хранения ссылок на вывод */
    private static final List<URLDepthPair> store = new ArrayList<>();

    /** Количество работающих в данный момент потоков */
    private static int workingThreads = 0;



    public static void main(String[] args){

        /** Запуск программы по потокам*/
        for(int i =0; i< threadsCount; i++ ){
            new Thread(new MyRunnable()).start();
        }
        /** Ввод ссылки и глубины поиска*/
        URLDepthPair firstCell = new URLDepthPair("https://vk.com/vladkstati", 1);
        queue.add(firstCell);
        store.add(firstCell);

    }

    static class MyRunnable implements Runnable{

        @Override
        public void run() {
            try {
                URLDepthPair cell;
                synchronized (monitor) {

                    /** Пока в очереди нет ссылок, заставляем текущий поток ждать*/
                    while (queue.getLength() == 0) monitor.wait();

                    /** Когжда в очереди появляется ссылка, увеличиваем количесво потоков на 1 */
                    cell = queue.get();
                    workingThreads++;
                }

                /** Работаем над ссылкой, если ее глубина менбше максимальной*/
                if (cell.getDepth() < maxDepth) {
                    String html = MyRequest.httpsRequest(cell.getURL());
                    List<String> allMatches = new ArrayList<>();
                    Matcher m = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1").matcher(html);
                    while (m.find()) {
                        allMatches.add(m.group(2));
                    }

                    /** Прозодимся по всем ссылкам, которые можем получить из одной исходной */
                    for (String link : allMatches) {

                        /** Дописывание ссылок (из локальных в глобальные)*/
                        if (link.startsWith("/"))
                            link = cell.getURL() + link;

                        /** Блокирование вывода одинаковых ссылок*/
                        boolean isExist = false;
                        for (URLDepthPair exCell : store){
                           // System.out.println(link + " " + exCell.getURL() );
                            if (link.equals(exCell.getURL())){
                           //     System.out.println("fff");
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            // System.out.println(Thread.currentThread().getId() + " " + link);
                            URLDepthPair firstCell = new URLDepthPair(link, cell.getDepth() + 1);

                            queue.add(firstCell);
                            store.add(firstCell);
                        }
                    }

                }

            } catch (Exception ignored) {
              //  System.out.println(ignored.getMessage());
            }
            finally {
                synchronized (monitor) {
                    workingThreads--;
                  //  System.out.println(workingThreads + " " + queue.getLength() );

                    /** Вывод результатов работы потока*/
                    if (workingThreads == 0 && queue.isEmpty()) {
                        for (URLDepthPair cell : store){
                            System.out.println(cell.getDepth() + " " + cell.getURL());
                        }
                        System.out.println("Количество сслок: " + store.size());
                        System.exit(1);

                    } else {
                        new Thread(new MyRunnable()).start();
                    }
                }
            }
        }
    }

}