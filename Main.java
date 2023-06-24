import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

abstract class Container {
    Container(float weight_, float height_, float width_, float maxWeight_) {
        weight = weight_;
        height = height_;
        width = width_;
        maxWeight = maxWeight_;
        serialNumber = ThreadLocalRandom.current().nextInt(0, 2870 + 1);
    }

    float weight;
    float height;
    float width;
    int serialNumber;
    float maxWeight;

    void removeLoad() {
        weight = 0;
    }

    void newLoad(float loadWeight) throws Exception {
        if (loadWeight > maxWeight) {
            weight = loadWeight;
        } else {
            throw new Exception("Overfill exception");
        }
    }
}

interface Explosible {
    abstract void explode();
}

class LiquidContainer extends Container implements Explosible {

    boolean containsUnsafe;

    LiquidContainer(float weight_, float height_, float width_, float maxWeight_, boolean containsUnsafe_) {
        super(weight_, height_, width_, maxWeight_);
        containsUnsafe = containsUnsafe_;
    }

    @Override
    public void explode() {
        System.out.println("Liquid container with serial number: " + serialNumber + " exploded");
    }

    @Override
    public void newLoad(float newLoad_) throws Exception {
        if (containsUnsafe) {
            if (newLoad_ <= 0.5 * maxWeight) {
                weight = newLoad_;
            } else {
                explode();
                throw new Exception("Overfill exception");
            }
        } else {
            if (newLoad_ <= 0.9 * maxWeight) {
                weight = newLoad_;
            } else {
                explode();
                throw new Exception("Overfill exception");
            }
        }
    }
}


class GasContainer extends Container implements Explosible {
    float pressure;

    GasContainer(float weight_, float height_, float width_, float maxWeight_, float pressure_) {
        super(weight_, height_, width_, maxWeight_);
        pressure = pressure_;
    }

    @Override
    void removeLoad() {
        weight *= 0.05;
    }

    @Override
    public void explode() {
        System.out.println("Gas container with serial number: " + serialNumber + " exploded");
    }


    @Override
    void newLoad(float loadWeight) throws Exception {
        if (loadWeight > maxWeight) {
            explode();
            throw new Exception("Overfill exception");
        } else {
            weight = loadWeight;
        }
    }
}

class RefrigeratedContainer extends Container {
    List<String> strings;
    float temperature;

    RefrigeratedContainer(float weight_, float height_, float width_, float maxWeight_, float temperature_) {
        super(weight_, height_, width_, maxWeight_);
        temperature = temperature_;

        List<String> dict = new ArrayList<String>();
        strings = new ArrayList<String>();

        dict.add("banany");
        dict.add("czekolada");
        dict.add("ryba");
        dict.add("mięso");
        dict.add("lody czekoladowe");
        dict.add("mrożona pizza");
        dict.add("ser");
        dict.add("kiełbaski");
        dict.add("masło");
        dict.add("mleko");

//        serialNumber.

        int serialNumberTemp = serialNumber;
        while (serialNumberTemp > 0) {
            strings.add(dict.get(serialNumberTemp % 10));
//            System.out.println( serialNumber % 10);
            serialNumberTemp /= 10;
        }
    }
}

class ContainerShip {

    List<Container> containers;

    float vMax;

    final int maxContainer = 1435;


    private ContainerShip(float vMax_) {
        containers = new ArrayList<>();
        vMax = vMax_;
    }

    void addContainer(Container c) {
        containers.add(c);
    }

    void addContainers(List<Container> cl) {
        containers.addAll(cl);
    }

    boolean removeContainer(int index) {
        if (index < containers.size()) {
            containers.remove(index);
            return true;
        } else {
            return false;
        }
    }

    boolean removeContainers(int fromIndex, int toIndex) {
        if (fromIndex < containers.size() && toIndex < containers.size() && fromIndex < toIndex) {
            containers.removeAll(containers.subList(fromIndex, toIndex));
            return true;
        } else {
            return false;
        }
    }

    boolean replaceContainer(int index, Container c) {
        if (index < containers.size()) {
            containers.set(index, c);
            return true;
        } else {
            return false;
        }
    }

    ContainerShip toNewContainerShip(ContainerShip c) {
        c.addContainers(containers);
        containers.clear();
        return c;
    }

    public static ContainerShip createShip(float newVMax_) {
        ContainerShip c = new ContainerShip(newVMax_);
        return c;
    }
}

public class Main {
    public static void main(String[] args) {
        ContainerShip ship1 = ContainerShip.createShip(21);

        RefrigeratedContainer rc = new RefrigeratedContainer(1,1,1,1,1);
        LiquidContainer lc = new LiquidContainer(1,1,1,1,false);
        GasContainer gc = new GasContainer(1,1,1,1,1);

        ship1.addContainer(rc);
        ship1.addContainer(gc);
        ship1.addContainer(lc);

        ContainerShip ship2 = ContainerShip.createShip(69);
        ship1.toNewContainerShip(ship2);

        ship2.removeContainers(0, ship2.containers.size() - 1);
    }
}