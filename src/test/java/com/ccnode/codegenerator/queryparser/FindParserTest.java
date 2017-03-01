package com.ccnode.codegenerator.queryparser;

import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFind;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFindDto;
import com.ccnode.codegenerator.methodnameparser.parser.FindParser;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author bruce.ge
 * @Date 2017/2/11
 * @Description
 */
public class FindParserTest {
    @Test
    public void testBasic() {
        List<String> prop = buildBasicProp();
        String methodName = "find";
        ParsedFindDto parse = new FindParser(methodName.toLowerCase(), prop).parse();
        assertThat(parse.getParsedFinds().size()).isEqualTo(1);
        ParsedFind parsedFind = parse.getParsedFinds().get(0);
        assertThat(parsedFind.getFetchProps()).isNull();
    }

    private List<String> buildBasicProp() {
        List<String> basicProps = Lists.newArrayList(
                "id",
                "userName",
                "password",
                "createTime",
                "updateTime"
        );
        return basicProps;
    }

    @Test
    public void testFindFirst() {
        List<String> props = buildProp();
        String methodName = "findFirst10OrderByName";
        FindParser findParser = new FindParser(methodName.toLowerCase(), props);
        ParsedFindDto parse = findParser.parse();
        List<ParsedFind> parsedFinds =
                parse.getParsedFinds();

    }

    @NotNull
    private List<String> buildProp() {
        List<String> props = new ArrayList<>();
        props.add("hello");
        props.add("first");
        props.add("id");
        props.add("name");
        props.add("order");
        return props;
    }
}
