package com.holub.database;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class XMLImporterTest { // 반드시 XMLExporterTest진행후 xml형식의 해당 파일이 만들어진 상태에서 테스트 해야함 . HTMLExporterTest 직후에
                               // 테스트하면 해당 파일내용이 html형식이라 오류발생
    @Test
    public void XMLImporter() throws IOException, ParserConfigurationException, SAXException {

        String file = "c:/dp2020/people"; // XMLExporter로 내보낸 people을 읽어보겠다.
        XMLImporter builder1 = new XMLImporter(file);
        builder1.startTable();
        assertThat(builder1.loadTableName(), is(equalTo("people")));
        assertThat(builder1.loadWidth(), is(equalTo(3)));
        String[][] row;
        row = builder1.loadRows();
        String[][] expected = { { "Allen", "Holub", "1" }, { "Ichabod", "Crane", "2" }, { "Rip", "VanWinkle", "3" },
                { "Goldie", "Locks", "4" } };
        assertThat(expected, is(row));

        String file2 = "c:/dp2020/university"; // XMLExporter로 내보낸 university을 읽어보겠다.
        XMLImporter builder2 = new XMLImporter(file2);
        builder2.startTable();
        assertThat(builder2.loadTableName(), is(equalTo("university")));
        assertThat(builder2.loadWidth(), is(equalTo(2)));
        String[][] row2;
        row2 = builder2.loadRows();
        String[][] expected2 = { { "chungang", "seoul" }, { "seoul", "seoul" }, { "woosong", "daejeon" } };
        assertThat(expected2, is(row2));

    }

}