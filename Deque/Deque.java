import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int sz = 0;
    
    private class Node { // if add <Item> following Node, type error thrown
        Item item;
        Node prev, next;
        
        public Node (Item item) {
            this.item = item;
        }
    }
    
    public Deque()  {    // construct an empty deque
        // do nothing or create an anchor
    }
    public boolean isEmpty() { return first == null; }
    public int size() { return sz; }
    public void addFirst(Item item) { 
        if (item == null) { throw new IllegalArgumentException(); }
        if (isEmpty()) { last = first = new Node(item); }
        else {
            Node oldFirst = first;
            first = new Node(item);
            first.next = oldFirst;
            oldFirst.prev = first;
        }
        sz++;
    }
    public void addLast(Item item) {
        if (item == null) { throw new IllegalArgumentException(); }
        if (isEmpty()) { first = last = new Node(item); }
        else {
            Node oldLast = last;
            last = new Node(item);
            oldLast.next = last;
            last.prev = oldLast;
        }
        sz++;
    }
    public Item removeFirst() { 
        if (isEmpty()) { throw new NoSuchElementException(); }
        Item item = first.item;
        first = first.next;
        if (first == null)
            last = null;
        else
            first.prev = null;
        sz--;
        return item;
    }
    public Item removeLast() { 
        if (isEmpty()) { throw new NoSuchElementException(); }
        Item item = last.item;
        last = last.prev;
        if (last == null)
            first = null;
        else
            last.next = null;
        sz--;
        return item;
    }
    public Iterator<Item> iterator() { 
        return new ListIterator();
    }   
    private class ListIterator implements Iterator<Item> {
        private Node cur = first;
        
        public boolean hasNext() { return cur != null; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) { throw new NoSuchElementException(); }
            Item item = cur.item;
            cur = cur.next;
            return item;
        }
    }
    private void printContents() {
        for (Item num : this) { StdOut.print(num + " "); }
        StdOut.println("");
    }
        
    public static void main(String[] args) {   // unit testing (optional)
        Deque<Integer> dq = new Deque<>();
        assert (dq.isEmpty());
        dq.addFirst(1);
        assert (!dq.isEmpty());
        assert (dq.size() == 1);
//        Iterator<Integer> it = dq.iterator();
        StdOut.print("added 1 to first: ");
        dq.printContents();
        
        dq.addFirst(0);
        assert (dq.size() == 2);
        StdOut.print("added 0 to first: ");
        dq.printContents();
        
        dq.addLast(2);
        assert (dq.size() == 3);
        StdOut.print("added 2 to last: ");
        dq.printContents();
        
        assert (dq.removeFirst() == 0);
        StdOut.print("remove from first: ");
        dq.printContents();
        
        assert (dq.removeLast() == 2);
        assert (dq.size() == 1);
        StdOut.print("remove from last: ");
        dq.printContents();
        
        assert (dq.removeLast() == 1);
        StdOut.print("remove from last: ");
        dq.printContents();
        assert (dq.isEmpty());
        
        try {
            dq.removeLast();
        } 
        catch (NoSuchElementException e) {
            StdOut.println("Exception catched: " + e);
        }
        
        
        for (int i = 1; i < 100; ++i) {
            dq.addFirst(i * i);
            if (i % 5 == 0) { dq.removeLast(); }
            assert (dq.size() == i - (i / 5));
        }
        assert (dq.size() == 80);
        dq.printContents();
        for (int i = 0; i < 80; ++i) { dq.removeFirst(); }
        assert (dq.isEmpty());
    }
}