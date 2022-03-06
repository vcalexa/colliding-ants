import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static java.math.RoundingMode.HALF_DOWN;
import static java.math.RoundingMode.HALF_UP;

public record Ant(BigDecimal position, Boolean direction) {
    static List<Ant> ants = new ArrayList<>();

    public static void main(String[] args) {
        BigDecimal time = BigDecimal.valueOf(0.0);
        for (int i = 0; i < 24; i++) {
            ants.add(new Ant(BigDecimal.valueOf(Math.random()).setScale(3, HALF_DOWN), Math.random() > 0.5));
        }
        String[] output = new String[101];
        for (int i = 0; i < 101; i++) {
            output[i] = ".";
        }
        while (someAntsLeft()) {

            for (Ant ant : ants) {
                BigDecimal antPos = ant.position.setScale(2, HALF_UP);
                var pos100 = antPos.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                String s = String.valueOf(pos100);
                int roundPos = Integer.parseInt(s.substring(0, s.indexOf(".")));
                System.out.println("position = " + ant.position + " ROUNDPOS= " + roundPos + " antSize" + ants.size());
                output[roundPos] = ant.direction ? ">" : "<";
            }
            for (int i = 0; i < 100; i++) {
                System.out.print(output[i]);
            }


            time = time.add(BigDecimal.valueOf(0.001));
            for (Ant ant : ants) {
                var antPos = ant.position;
                var newPos = ant.direction ? antPos.add(time) : antPos.subtract(time);
                if (newPos.compareTo(BigDecimal.ZERO) < 0 || newPos.compareTo(BigDecimal.ONE) > 0) {
                    output[ants.indexOf(ant)] = ".";
                    ants.remove(ant);

                    //System.out.println("Removed one ant " + ants.size());
                } else {
                    //System.out.println("ant updated");
                    ants.remove(ant);
                    ants.add(new Ant(newPos, ant.direction));
                    reverseDirection(ant);
                }
                break;
            }
            System.out.println(time);
        }
    }

    private static boolean someAntsLeft() {
        boolean someAnts = false;
        for (Ant ant : ants) {
            if (ant.position.compareTo(BigDecimal.ZERO) > 0 && ant.position.compareTo(BigDecimal.ONE) < 0) {
                someAnts = true;
                break;
            }
        }
        return someAnts;
    }

    private static void reverseDirection(Ant a) {

        for (int i = 0; i < ants.size(); i++) {
            if (a.direction.equals(ants.get(i).direction)) {
                ants.remove(a);
                ants.add(new Ant(a.position, !a.direction));

                ants.remove(i);
                ants.set(i, new Ant(ants.get(i).position, !ants.get(i).direction));
                break;
            }
        }
    }

}
