import java.io.IOException;

public class StudentManagerTest extends student.TestCase{

    public void setUp() throws IOException {
        MemoryManager test = new MemoryManager("output.data", 128);
    }
    
    
    public void testMain() throws Exception{
        /**
        MemoryManager test = new MemoryManager("output.data", 128);
        test.insertName("906088299", "Ryan Kirkpatrick");
        test.insertName("983927289", "Bryan K");
        test.insertName("193745998", "Brianna Yoyo");
        test.insertName("937659374", "Batman Kirkpatrick");
        test.insertName("239847598", "Taco Kirkpatrick");
        test.searchHash("906088299");
        test.searchHash("193745998");
        System.out.println("------------------------");
        test.print();
        System.out.println("------------------------");
        test.insertEssay("906088299", "this is an essay and asdfsd");
        System.out.println(test.clear("906088299"));
        System.out.println(test.remove("983927289"));
        test.insertName("983927289", "Bryan K");
        test.print();
        System.out.println(test.remove("906088299"));
        test.insertName("906088299", "my name");
        test.print();*/
   }
}
