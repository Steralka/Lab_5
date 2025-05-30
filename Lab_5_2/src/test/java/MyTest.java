import org.example.managers.CSVParser;
import org.example.managers.CollectionManager;
import org.example.models.*;
import org.example.utility.Console;
import org.example.utility.StandardConsole;
import org.junit.Test;
import org.junit.Assert;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MyTest {
    private static final LabWork DEFAULT_LAB_WORK = new LabWork(
            1L,
            "Lab1",
            new Coordinates(0, 0),
            5.0,
            Difficulty.EASY,
            new Discipline("Discipline1", 0L, 0L)
    );

    private static final LabWork NULLS_LAB_WORK = new LabWork(
            2L,
            "Lab2",
            new Coordinates(0, 0),
            null,
            Difficulty.EASY,
            null
    );

    @Test
    public void testCSVParser() {
        final String fileName = "testCSVParser";
        try {
            CSVParser parser = new CSVParser(fileName, new StandardConsole());

            Collection<LabWork> collection1 = new ArrayList<>();
            collection1.add(DEFAULT_LAB_WORK);
            collection1.add(NULLS_LAB_WORK);
            parser.writeCollection(collection1);

            Collection<LabWork> collection2 = new ArrayList<>();
            parser.readCSVToCollection(collection2);

            Files.delete(Path.of(fileName + CSVParser.FILE_EXTENSION));

            Assert.assertArrayEquals(collection1.toArray(), collection2.toArray());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testIncorrectInputData() {
        final String fileName = "testIncorrectInputData";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + CSVParser.FILE_EXTENSION))) {

            writer.write("-1,Lab1,5,3,2024-03-29T09:32:21.002961100+03:00[Europe/Moscow],5.0,EASY,Discipline1,0,0");
            writer.newLine();
            writer.write("1,,5,3,2024-03-29T09:32:21.002961100+03:00[Europe/Moscow],5.0,EASY,Discipline1,0,0");
            writer.newLine();
            writer.write("2,Lab1,10,3,2024-03-29T09:32:21.002961100+03:00[Europe/Moscow],5.0,EASY,,0,0");
            writer.newLine();
            writer.write("3,Lab1,10,3,2024-03-29T09:32:21.002961100+03:00[Europe/Moscow],5.0,INCORRECT,Discipline1,0,0");
            writer.newLine();
            writer.write("4,Lab17,10,3,2032-03-29T09,5.0,EASY,Discipline1,0,0");
            writer.newLine();
            writer.write(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");

            writer.close();

            CSVParser parser = new CSVParser(fileName, new StandardConsole());
            Collection<LabWork> collection = new ArrayList<>();
            parser.readCSVToCollection(collection);

            Files.delete(Path.of(fileName + CSVParser.FILE_EXTENSION));

            Assert.assertEquals(0, collection.size());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testCommands() {
        final long count_add = 100;
        final String fileName = "testCommands";

        Console console = new StandardConsole();
        CSVParser csvParser = new CSVParser(fileName, console);
        CollectionManager collectionManager = new CollectionManager(csvParser);

        long lastTakenId = collectionManager.getFreeId();
        Double maxMinimalPoint = null;
        int countNullMinimalPoint = 0;
        int countSameMinimalPoint = 0;
        double same = 2.0;
        for (long i = 1; i <= count_add; i++) {
            lastTakenId = collectionManager.getFreeId();
            Double minimalPoint = i % 2 == 0 ? (double) i : null;

            collectionManager.add(new LabWork(
                    lastTakenId,
                    String.valueOf(i),
                    new Coordinates(5, 2),
                    minimalPoint,
                    Difficulty.EASY,
                    new Discipline("d" + i, 10, 20)
            ));

            if (Objects.isNull(minimalPoint)) {
                countNullMinimalPoint++;
            } else if (minimalPoint == same) {
                countSameMinimalPoint++;
            }

            if (Objects.isNull(maxMinimalPoint) || (Objects.nonNull(minimalPoint) && minimalPoint > maxMinimalPoint)) {
                maxMinimalPoint = minimalPoint;
            }
        }

        Assert.assertEquals(count_add, collectionManager.size());
        Assert.assertTrue(collectionManager.isMaxElement(new LabWork(
                lastTakenId,
                "name",
                new Coordinates(5, 2),
                Objects.isNull(maxMinimalPoint) ? 1.0 : maxMinimalPoint + 1.0,
                Difficulty.EASY,
                null
        )));
        Assert.assertEquals(countNullMinimalPoint, collectionManager.countEqualMinimalPoint(null));
        Assert.assertEquals(countSameMinimalPoint, collectionManager.countEqualMinimalPoint(same));

        int sizeBefore = collectionManager.size();
        collectionManager.remove(collectionManager.getFreeId());
        Assert.assertEquals(sizeBefore, collectionManager.size());

        if (collectionManager.contains(collectionManager.getLabWorkById(lastTakenId))) {
            collectionManager.remove(lastTakenId);
            Assert.assertEquals(sizeBefore - 1, collectionManager.size());
        }

        collectionManager.clear();
        Assert.assertTrue(collectionManager.isEmpty());
    }
}
