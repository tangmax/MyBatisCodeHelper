package com.ccnode.codegenerator.queryparser;

import com.ccnode.codegenerator.methodnameparser.parsedresult.update.ParsedUpdateDto;
import com.ccnode.codegenerator.methodnameparser.parser.UpdateParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/3/1
 * @Description
 */
public class UpdateParserTest {
    @Test
    public void testParse(){
        String methodName = "updateIdAndNameById";
        List<String> props = new ArrayList<>();
        props.add("Id");
        props.add("Name");
        props.add("username");
        ParsedUpdateDto parse = new UpdateParser(methodName.toLowerCase(), props).parse();
        parse.getUpdateList();
    }
}
