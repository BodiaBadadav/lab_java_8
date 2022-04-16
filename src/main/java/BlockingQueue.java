import java.util.ArrayList;
import java.util.List;
/** блокирцующая очередь
 * Нужна чтобы оповещать потоки о том, что очередь пополнилась
 * */
public class BlockingQueue {

    /** объявляем поля */
    private final List<URLDepthPair> queue = new ArrayList<>();
    private final Object monitor;

    /** конструктор */
    public BlockingQueue(Object monitor){
        this.monitor = monitor;
    }


    /** Метод, добавляющий ячейку в очередь, и оповещающий об этом следующий поток */

    public void add(URLDepthPair cell){
        synchronized (monitor) {
            queue.add(cell);
            monitor.notify();
        }
    }

    /**  Метод, проверяющий, пуста ли очередь */
    public boolean isEmpty (){
        return queue.isEmpty();
    }

    /** Метод возвращает первый элемент очереди */
    public URLDepthPair get(){
        synchronized (monitor) {
            URLDepthPair cell = queue.get(0);
            queue.remove(cell);
            return cell;
        }
    }

    /** Метод, возвращающий размер очереди */
    public int getLength(){
        synchronized (monitor) {
            return queue.size();
        }
    }
}
