package ed.lab;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class E03AVLTreeTest {

    @ParameterizedTest
    @MethodSource("testArguments")
    void test(List<AVLOperations> operations) {
        List<AVLOperations> previousOps = new LinkedList<>();

        E03AVLTree<Integer> avlTree = new E03AVLTree<>(Integer::compare);

        for (AVLOperations operation : operations) {
            final Integer result =
                    OPERATORS.get(operation.operationName().toLowerCase())
                            .apply(avlTree, operation.argument());

            previousOps.add(operation);

            assertEquals(operation.expected(), result,
                    () -> String.format("Error al llamar a %s(). Se esperaba %s pero se obtuvo %s.\nLista de operaciones previas:\n%s",
                            operation.operationName(),
                            operation.expected(),
                            result,
                            previousOps.stream()
                                    .map(String::valueOf)
                                    .collect(Collectors.joining("\n"))));
        }
    }

    private static Stream<List<AVLOperations>> testArguments() {
        try {
            final String fileContent = new String(
                    Objects.requireNonNull(E03AVLTreeTest.class.getClassLoader()
                                    .getResourceAsStream("ed/lab/E03.csv"))
                            .readAllBytes());

            final String[] lines = fileContent.split("\n");

            Stream.Builder<List<AVLOperations>> builder = Stream.builder();

            List<AVLOperations> operations = new LinkedList<>();

            for (String line : lines) {
                if (line.isBlank()) {
                    builder.add(operations);
                    operations = new LinkedList<>();
                    continue;
                }

                String[] commands = line.split(",");

                final Integer arg = Optional.ofNullable(commands[1])
                        .map(String::trim)
                        .filter(command -> !"null".equalsIgnoreCase(command))
                        .map(Integer::parseInt)
                        .orElse(null);

                final Integer expected = Optional.ofNullable(commands[2])
                        .map(String::trim)
                        .filter(command -> !"null".equalsIgnoreCase(command))
                        .map(Integer::parseInt)
                        .orElse(null);

                final AVLOperations avlOperation = new AVLOperations(commands[0], arg, expected);

                operations.add(avlOperation);
            }

            if (!operations.isEmpty())
                builder.add(operations);

            return builder.build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private record AVLOperations(String operationName, Integer argument, Integer expected) {
        @Override
        public String toString() {
            return String.format("%s(%s)%s",
                    operationName,
                    argument != null ? argument : "",
                    expected != null ? " [Debe retornar: " + expected + "]" : "");
        }
    }

    private static final Map<String, BiFunction<E03AVLTree<Integer>, Integer, Integer>> OPERATORS = Map.of(
            "search", E03AVLTree::search,
            "height", (avlTree, arg) -> avlTree.height(),
            "size", (avlTree, arg) -> avlTree.size(),
            "insert", (avlTree, arg) -> {
                avlTree.insert(arg);
                return null;
            },
            "delete",
            (avlTree, arg) -> {
                avlTree.delete(arg);
                return null;
            }
//si
    );
}