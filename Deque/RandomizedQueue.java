import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int first = 0;
    private int last = 0;
    
    public RandomizedQueue() {  // construct an empty randomized queue
        s = (Item[]) new Object[10];
    }
    public boolean isEmpty() {                // is the queue empty?
        return first == last && s[first] == null;
    }
    public int size() {  // return the number of items on the queue
        if (isEmpty()) { return 0; }
        else if (last >= first) { return last - first + 1; }
        else { return last - first + 1 + s.length; }
    }
    public void enqueue(Item item) {         // add the item
        if (item == null) { throw new IllegalArgumentException(); }
        if (size() == s.length) { resize(s.length * 2); }
        if (isEmpty()) { s[first] = item; }
        else {
            last = (last + 1) % s.length;
            s[last] = item; 
        }
//        StdOut.println("first " + first + ", last " + last);
    }   
    public Item dequeue() {  // remove and return a random item
        if (isEmpty()) { throw new NoSuchElementException(); }
        // randomize dequeuing: bug! do modulo
        int toDequeue = StdRandom.uniform(first, first + size()) % s.length;       
        Item item = s[toDequeue];
        s[toDequeue] = s[first];
        s[first] = null;
        if (size() > 1) { first = (first + 1) % s.length; }
        // after removing the only element, don't resize
        if (size() > 0 && size() <= s.length / 4) { resize(s.length / 2); }
//        StdOut.println("first " + first + ", last " + last + ", size " + size()
//                      + ", capacity " + s.length);
        return item;
    }
    private void resize(int newCapacity) {
        Item[] newArray = (Item[]) new Object[newCapacity];
        for (int i = 0; i < size(); ++i) {
            newArray[i] = s[(first + i) % s.length];
        }
        last = size() - 1; // be careful of the order
        first = 0;        
        s = newArray;
    }
    public Item sample() {    // return (but do not remove) a random item
        if (isEmpty()) throw new NoSuchElementException();
        return s[(first + StdRandom.uniform(size())) % s.length];
    }
    public Iterator<Item> iterator() { // return an independent iterator over items in random order
        return new RandomIterator();
    }
    private class RandomIterator implements Iterator<Item> {
        private Item[] seq;
        private int cur = 0;
        
        public RandomIterator() {
            // a little copy paste
            seq = (Item[]) new Object[size()];
            for (int i = 0; i < size(); ++i) {
                seq[i] = s[(first + i) % s.length];
            }
            StdRandom.shuffle(seq);
        }
        public boolean hasNext() { return cur < seq.length; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) { throw new NoSuchElementException(); }
            return seq[cur++]; 
        }
    }
    private void printContentsInOrder() {
        for (int i = first; i < first + size(); ++i) {
            StdOut.print(s[i % s.length] + " ");
        }
        StdOut.println("");
    }
    public static void main(String[] args) { // unit testing (optional)
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        assert (rq.isEmpty());
        assert (rq.size() == 0);
        
        rq.enqueue(1);
        assert (rq.size() == 1);
        assert (!rq.isEmpty());
        StdOut.print("enqueued 1: ");
        rq.printContentsInOrder();
        
        rq.enqueue(2);
        assert (rq.size() == 2);
        StdOut.print("enqueued 2: ");
        rq.printContentsInOrder();
        
        rq.enqueue(3);
        assert (rq.size() == 3);
        StdOut.print("enqueued 3: ");
        rq.printContentsInOrder();
        
        StdOut.print("random iterator test: ");
        for (int num : rq) StdOut.print(num + " ");
        StdOut.println("");
        
        rq.dequeue();
        assert (rq.size() == 2);
        StdOut.print("dequeued: ");
        rq.printContentsInOrder();
        
        rq.dequeue();
        assert (rq.size() == 1);
        StdOut.print("dequeued: ");
        rq.printContentsInOrder();
        
        rq.dequeue();
        assert (rq.size() == 0);
        StdOut.print("dequeued: ");
        rq.printContentsInOrder();
        assert (rq.isEmpty());
        
        // bigger test
        StdOut.println("100 number test: ");
        for (int i = 0; i < 100; ++i) {
            rq.enqueue(i);
            if (i % 5 == 0) { rq.dequeue(); }
            assert (rq.size() == i - i / 5);
        }
        StdOut.print("sample test: ");
        for (int i = 0; i < 10; ++i) StdOut.print(rq.sample() + " ");
        StdOut.println("");
        
        for (int i = 0; i < 80; ++i) {
            assert (rq.size() == 80 - i);
            rq.dequeue();
        }
        assert (rq.isEmpty());
    }
}