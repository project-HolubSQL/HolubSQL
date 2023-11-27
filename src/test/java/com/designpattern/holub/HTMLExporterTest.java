
package com.designpattern.holub;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xml.sax.SAXException;

import com.designpattern.database.HTMLExporter;
import com.designpattern.database.Table;
import com.designpattern.database.TableFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HTMLExporterTest {
  Logger logger = LoggerFactory.getLogger(HTMLExporterTest.class);

  @Test
  public void testHTMLExporter() throws IOException {
    Table people = TableFactory.create("people",
        new String[] { "First", "Last", "Id" });
    people.insert(new String[] { "Allen", "Holub", "1" });
    people.insert(new String[] { "Ichabod", "Crane", "2" });
    people.insert(new String[] { "Rip", "VanWinkle", "3" });
    people.insert(new String[] { "Goldie", "Locks", "4" });

    // file out
    HTMLExporter builder1 = new HTMLExporter(new FileWriter("people_output.html"));
    people.export(builder1);

  }
}
