import java.util.HashSet;
import java.util.NoSuchElementException;

public class FactoryImpl implements Factory {
    private Holder first;
    private Holder last;
    private Integer size;

    @Override
    public void addFirst(Product product) {
        if (size == null || size == 0) {
            Holder holder = new Holder(null, product, null);
            first = last = holder;
            size = 1;
        }else {
            Holder holder = new Holder(null, product, first);
            first.setPreviousHolder(holder);
            first = holder;
            size++;
        }
    }

    @Override
    public void addLast(Product product) {
        if (size == null || size == 0) {
            Holder holder = new Holder(null, product, null);
            first = last = holder;
            size = 1;
        }else {
            Holder holder = new Holder(last, product, null);
            last.setNextHolder(holder);
            last = holder;
            size++;
        }
    }

    @Override
    public Product removeFirst() throws NoSuchElementException {
        if (size == null || size == 0) throw new NoSuchElementException();
        Product product = first.getProduct();
        if (size == 1) {
            last = null;
            first.setProduct(null);
            first = null;
        }else {
            first = first.getNextHolder();
            first.getPreviousHolder().setNextHolder(null);
            first.getPreviousHolder().setProduct(null);
            first.setPreviousHolder(null);
        }
        size--;
        return product;
    }

    @Override
    public Product removeLast() throws NoSuchElementException {
        if (size == null || size == 0) throw new NoSuchElementException();
        Product product = last.getProduct();
        if (size == 1) {
            first = null;
            last.setProduct(null);
            last = null;
        }else {
            last = last.getPreviousHolder();
            last.getNextHolder().setPreviousHolder(null);
            last.getNextHolder().setProduct(null);
            last.setNextHolder(null);
        }
        size--;
        return product;
    }

    @Override
    public Product find(int id) throws NoSuchElementException {
        if (size == null || size == 0) throw new NoSuchElementException();
        if (first.getProduct().getId() == id) return first.getProduct();
        if (size > 1) {
            Holder holder = first.getNextHolder();
            while (holder != null) {
                if (holder.getProduct().getId() == id) return holder.getProduct();
                holder = holder.getNextHolder();
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public Product update(int id, Integer value) throws NoSuchElementException {
        if (size == null || size == 0) throw new NoSuchElementException();
        if (first.getProduct().getId() == id) {
            Product product = new Product(first.getProduct().getId(), first.getProduct().getValue());
            first.getProduct().setValue(value);
            return product;
        }
        if (size > 1) {
            Holder holder = first.getNextHolder();
            while (holder != null) {
                if (holder.getProduct().getId() == id) {
                    Product product = new Product(holder.getProduct().getId(), holder.getProduct().getValue());
                    holder.getProduct().setValue(value);
                    return product;
                }
                holder = holder.getNextHolder();
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public Product get(int index) throws IndexOutOfBoundsException {
        if (size == null || size == 0) throw new IndexOutOfBoundsException();
        else if (index >= size || index < 0) throw new IndexOutOfBoundsException();
        if (index == 0) return first.getProduct();
        Holder holder = first;
        for (int i = 0; i < index; i++) {
            holder = holder.getNextHolder();
        }
        return holder.getProduct();
    }

    @Override
    public void add(int index, Product product) throws IndexOutOfBoundsException {
        if (size == null && index > 0) throw new IndexOutOfBoundsException();
        else if ((size != null && index > size) || index < 0) throw new IndexOutOfBoundsException();
        else if (index == 0) addFirst(product);
        else if (index == size) addLast(product);
        else {
            Holder holder = first;
            for (int i = 0; i < index; i++) {
                holder = holder.getNextHolder();
            }
            size++;
            Holder newHolder = new Holder(holder.getPreviousHolder(), product, holder);
            holder.getPreviousHolder().setNextHolder(newHolder);
            holder.setPreviousHolder(newHolder);
        }
    }

    @Override
    public Product removeIndex(int index) throws IndexOutOfBoundsException {
        if (size == null || size == 0) throw new IndexOutOfBoundsException();
        else if (index >= size || index < 0) throw new IndexOutOfBoundsException();
        else if (index == 0) return removeFirst();
        else if (index == size - 1) return removeLast();
        else {
            Holder holder = first;
            for (int i = 0; i < index; i++) {
                holder = holder.getNextHolder();
            }
            size--;
            holder.getPreviousHolder().setNextHolder(holder.getNextHolder());
            holder.getNextHolder().setPreviousHolder(holder.getPreviousHolder());
            holder.setPreviousHolder(null);
            holder.setNextHolder(null);
            Product product = holder.getProduct();
            holder.setProduct(null);
            return product;
        }
    }

    @Override
    public Product removeProduct(int value) throws NoSuchElementException {
        if (size == null || size == 0) throw new NoSuchElementException();
        if (first.getProduct().getValue() == value) return removeFirst();
        if (size > 1) {
            Holder holder = first.getNextHolder();
            while (holder.getNextHolder() != null) {
                if (holder.getProduct().getValue() == value) {
                    size--;
                    holder.getPreviousHolder().setNextHolder(holder.getNextHolder());
                    holder.getNextHolder().setPreviousHolder(holder.getPreviousHolder());
                    holder.setPreviousHolder(null);
                    holder.setNextHolder(null);
                    Product product = holder.getProduct();
                    holder.setProduct(null);
                    return product;
                }
                holder = holder.getNextHolder();
            }
            if (last.getProduct().getValue() == value) return removeLast();
        }
        throw new NoSuchElementException();
    }

    @Override
    public int filterDuplicates() {
        int removedProducts = 0;
        Holder removedHolder;
        HashSet<Integer> values = new HashSet<>();
        Holder holder = first;
        while (holder != null) {
            if (holder.equals(last)) {
                if (values.contains(holder.getProduct().getValue())) {
                    removedHolder = new Holder(holder.getPreviousHolder(), holder.getProduct(), holder.getNextHolder());
                    last = last.getPreviousHolder();
                    removedHolder.getPreviousHolder().setNextHolder(null);
                    removedHolder.setPreviousHolder(null);
                    removedHolder.setProduct(null);
                    removedProducts++;
                    size--;
                }
                break;
            }
            else if (values.contains(holder.getProduct().getValue())) {
                removedHolder = new Holder(holder.getPreviousHolder(), holder.getProduct(), holder.getNextHolder());
                holder = holder.getNextHolder();
                removedHolder.getPreviousHolder().setNextHolder(removedHolder.getNextHolder());
                removedHolder.getNextHolder().setPreviousHolder(removedHolder.getPreviousHolder());
                removedHolder.setPreviousHolder(null);
                removedHolder.setNextHolder(null);
                removedHolder.setProduct(null);
                removedProducts++;
                size--;
            }
            else {
                values.add(holder.getProduct().getValue());
                holder = holder.getNextHolder();
            }
        }
        return removedProducts;
    }

    @Override
    public void reverse() {
        if (!(size == null || size == 0)) {
            Holder oldNext;
            Holder oldFirst = first;
            first = last;
            last = oldFirst;
            Holder holder = first;
            while (holder != null) {
                oldNext = holder.getNextHolder();
                holder.setNextHolder(holder.getPreviousHolder());
                holder.setPreviousHolder(oldNext);
                holder = holder.getNextHolder();
            }
        }
    }

    public void print() {
        StringBuilder line = new StringBuilder();
        if (size == null || size == 0) System.out.println("{}");
        else {
            Holder holder = first;
            line.append("{");
            for (int i = 0; i < size; i++) {
                if (i != 0) line.append(",");
                line.append(holder.getProduct().toString());
                holder = holder.getNextHolder();
            }
            line.append("}");
            System.out.println(line);
        }
    }
}