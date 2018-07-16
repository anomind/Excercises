import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Main {

    private static final String DELIMITER = ", ";

    public static void main(String[] argv) {
        test1();
        test2();
    }

    private static String concatSorted(Collection<Pair<Integer, String>> pairs) {
        return pairs.stream().sorted(Comparator.comparingInt(Pair::getFirst))
                .map(pair -> new StringBuilder().append(pair.getSecond()))
                .collect(Collectors.joining(DELIMITER));
    }

    public  static <A extends Number, B> void handle(Event<Pair<? extends A, ? extends B>> event, BiConsumer<? super A, ? super B> consumer) {
        consumer.accept(event.getData().getFirst(), event.getData().getSecond());
    }

    private static void test1() {
        Pair<Integer, String> p1 = new Pair<>(2, "bb");
        Pair<Integer, String> p2 = new Pair<>(1, "aa");

        System.out.println(concatSorted(Arrays.asList(p1, p2)));
        System.out.println(concatSorted(Arrays.asList(p2, p1)));
        System.out.println(concatSorted(Collections.singleton(p1)));
        System.out.println(concatSorted(Collections.emptyList()));
    }

    private static void test2() {
        Pair<Integer, String> p = new Pair<>(1, "bb");

        //  следующие вызовы должны "сходиться" без ошибок
        handle(new Event<>(p), (a, b) -> {});
        handle(new Event<>(p), (Object a, Object b) -> {});
        handle(new Event<>(p), (Object a, String b) -> {});
        handle(new Event<>(p), (Number a, Object b) -> {});
        handle(new Event<>(p), (Integer a, Object b) -> {});
        handle(new Event<>(p), (Integer a, String b) -> {});

        Pair<Double, String> p2 = new Pair<>(1.0, "bb");
        handle(new Event<>(p2), (Integer a, String b) -> {});   // здесь должна быть ошибка так как получатель не может обработать Double

        Pair<Boolean, String> p3 = new Pair<>(true, "bb");
        handle(new Event<>(p3), (a, b) -> {});   // здесь должна быть ошибка так как первый параметр пары имеет нечисловой тип
    }
}