package oy.tol.tra;

public class QueueImplementation<E> implements QueueInterface<E> {

   private static final int DEFAULT_QUEUE_SIZE = 10;
   private Object [] itemArray;
   private int capacity;
   private int head;
   private int tail;
   private int count;

   public QueueImplementation() {
      this(DEFAULT_QUEUE_SIZE);
   }

   public QueueImplementation(int capacity) throws QueueAllocationException {
      if (capacity < 2) {
         throw new QueueAllocationException("Queue capacity must be at least two");
      }
      try {
         itemArray = new Object[capacity];
         this.capacity = capacity;
      } catch (OutOfMemoryError error) {
         throw new QueueAllocationException("Could not allocate queue");
      }
   }

   @Override
   public int capacity() {
      return capacity;
   }

   @Override
   public void enqueue(E element) throws QueueAllocationException, NullPointerException {
      if (element == null) {
         throw new NullPointerException("Element must not be null");
      }
      if (count == capacity) {
         grow();
      }
      itemArray[tail] = element;
      tail = (tail + 1) % capacity;
      count++;
   }

   private void grow() throws QueueAllocationException {
      try {
         Object [] largerArray = new Object[capacity * 2];
         for (int index = 0; index < count; index++) {
            largerArray[index] = itemArray[(head + index) % capacity];
         }
         itemArray = largerArray;
         capacity = largerArray.length;
         head = 0;
         tail = count;
      } catch (OutOfMemoryError error) {
         throw new QueueAllocationException("Could not grow queue");
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public E dequeue() throws QueueIsEmptyException {
      if (isEmpty()) {
         throw new QueueIsEmptyException("Queue is empty");
      }
      E element = (E)itemArray[head];
      itemArray[head] = null;
      head = (head + 1) % capacity;
      count--;
      if (count == 0) {
         head = 0;
         tail = 0;
      }
      return element;
   }

   @SuppressWarnings("unchecked")
   @Override
   public E element() throws QueueIsEmptyException {
      if (isEmpty()) {
         throw new QueueIsEmptyException("Queue is empty");
      }
      return (E)itemArray[head];
   }

   @Override
   public int size() {
      return count;
   }

   @Override
   public boolean isEmpty() {
      return count == 0;
   }

   @Override
   public void clear() {
      for (int index = 0; index < capacity; index++) {
         itemArray[index] = null;
      }
      head = 0;
      tail = 0;
      count = 0;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder("[");
      for (int index = 0; index < count; index++) {
         if (index > 0) {
            builder.append(", ");
         }
         builder.append(itemArray[(head + index) % capacity]);
      }
      builder.append("]");
      return builder.toString();
   }
}
