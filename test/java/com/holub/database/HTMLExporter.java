// package com.holub.database;

// import org.junit.Test;

// import java.io.*;

// import static org.hamcrest.core.Is.is;
// import static org.hamcrest.core.IsEqual.equalTo;
// import static org.junit.Assert.assertThat;

// public class HTMLExporterTest {
// @Test
// public void HTML() throws IOException {
// Table people = TableFactory.create("people",
// new String[] { "First", "Last", "Id" });
// people.insert(new String[] { "Allen", "Holub", "1" });
// people.insert(new String[] { "Ichabod", "Crane", "2" });
// people.insert(new String[] { "Rip", "VanWinkle", "3" });
// people.insert(new String[] { "Goldie", "Locks", "4" });
// Writer out = new FileWriter("c:/dp2020/people");
// HTMLExporter builder1 = new HTMLExporter(out);
// people.export(builder1);
// out.close();
// File file = new File("c:/dp2020/people");
// StringBuffer stringBuffer = new StringBuffer();
// FileReader fileReader = new FileReader(file);
// int index = 0;
// while ((index = fileReader.read()) != -1) {
// stringBuffer.append((char) index);

// }
// // HTMLExporter테스트
// assertThat(stringBuffer.toString(), is(equalTo(
// "<html><body>people<table
// border=\"1\"><th>First</th><th>Last</th><th>Id</th><tr><td>Allen</td><td>Holub</td>"
// +
// "<td>1</td></tr><tr><td>Ichabod</td><td>Crane</td><td>2</td></tr><tr><td>Rip</td><td>VanWinkle</td><td>3</td></tr><tr><td>Goldie</td><td>Locks</td><td>4</td></tr></table></body></html>")));

// builder1.accept(new getFileVisitor("people", people));
// builder1.accept(new decorateVisitor("people"));
// File file1 = new File("c:/dp2020/people.html");
// StringBuffer stringBuffer1 = new StringBuffer();
// FileReader fileReader1 = new FileReader(file1);
// int index1 = 0;
// while ((index1 = fileReader1.read()) != -1) {
// stringBuffer1.append((char) index1);

// }
// // getFileVisitor,decorateVisitor테스트
// assertThat(stringBuffer1.toString(), is(equalTo(
// "<html><body>people<table border=\"1\"
// bordercolor=\"blue\"><th>First</th><th>Last</th><th>Id</th><tr><td>Allen</td><td>Holub</td>"
// +
// "<td>1</td></tr><tr><td>Ichabod</td><td>Crane</td><td>2</td></tr><tr><td>Rip</td><td>VanWinkle</td><td>3</td></tr><tr><td>Goldie</td><td>Locks</td><td>4</td></tr></table></body></html>")));

// fileReader.close();
// fileReader1.close();

// System.out.println("-------------------------------------------");

// Table university = TableFactory.create("university",
// new String[] { "name", "location" });
// university.insert(new String[] { "chungang", "seoul" });
// university.insert(new String[] { "seoul", "seoul" });
// university.insert(new String[] { "woosong", "daejeon" });
// Writer out1 = new FileWriter("c:/dp2020/university");
// HTMLExporter builder2 = new HTMLExporter(out1);
// university.export(builder2);
// out1.close();
// File file2 = new File("c:/dp2020/university");
// StringBuffer stringBuffer2 = new StringBuffer();
// FileReader fileReader2 = new FileReader(file2);
// int index2 = 0;
// while ((index2 = fileReader2.read()) != -1) {
// stringBuffer2.append((char) index2);

// }
// // HTMLExporter테스트
// assertThat(stringBuffer2.toString(), is(equalTo(
// "<html><body>university<table
// border=\"1\"><th>name</th><th>location</th><tr><td>chungang</td><td>seoul</td></tr><tr><td>seoul</td><td>seoul</td>"
// +
// "</tr><tr><td>woosong</td><td>daejeon</td></tr></table></body></html>")));

// builder2.accept(new getFileVisitor("university", university));
// // builder2.accept(new decorateVisitor("university"));
// StringBuffer stringBuffer3 = new StringBuffer();
// File file3 = new File("c:/dp2020/university.html");
// FileReader fileReader3 = new FileReader(file3);
// int index3 = 0;
// while ((index3 = fileReader3.read()) != -1) {
// stringBuffer3.append((char) index3);

// }
// // getFileVisitor, decorateVisitor테스트
// assertThat(stringBuffer3.toString(), is(equalTo(
// "<html><body>university<table
// border=\"1\"><th>name</th><th>location</th><tr><td>chungang</td><td>seoul</td></tr><tr>"
// +
// "<td>seoul</td><td>seoul</td>" +
// "</tr><tr><td>woosong</td><td>daejeon</td></tr></table></body></html>")));

// fileReader2.close();
// fileReader3.close();

// }

// }