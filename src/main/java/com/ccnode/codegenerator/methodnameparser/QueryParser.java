package com.ccnode.codegenerator.methodnameparser;

import com.ccnode.codegenerator.methodnameparser.buidler.QueryBuilder;
import com.ccnode.codegenerator.methodnameparser.parsedresult.count.ParsedCountDto;
import com.ccnode.codegenerator.methodnameparser.parsedresult.delete.ParsedDeleteDto;
import com.ccnode.codegenerator.methodnameparser.parsedresult.find.ParsedFindDto;
import com.ccnode.codegenerator.methodnameparser.parsedresult.update.ParsedUpdateDto;
import com.ccnode.codegenerator.methodnameparser.parser.CountParser;
import com.ccnode.codegenerator.methodnameparser.parser.DeleteParser;
import com.ccnode.codegenerator.methodnameparser.parser.FindParser;
import com.ccnode.codegenerator.methodnameparser.parser.UpdateParser;
import com.ccnode.codegenerator.pojo.MethodXmlPsiInfo;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryParser {

    //hard to test when use with psiclass.
    public static QueryParseDto parse(List<String> props, MethodXmlPsiInfo info) {
        //make it cool to start.
        String methodLower = info.getMethodName().toLowerCase();
        if (methodLower.startsWith(KeyWordConstants.FIND)) {
            ParsedFindDto parse = new FindParser(methodLower, props).parse();
            //then build the result by it make it happen.
            return QueryBuilder.buildFindResult(parse.getParsedFinds(), parse.getParsedFindErrors(), info);
        } else if (methodLower.startsWith(KeyWordConstants.UPDATE)) {
            ParsedUpdateDto dto = new UpdateParser(methodLower, props).parse();
            //then build the result by list to control.
            return QueryBuilder.buildUpdateResult(dto.getUpdateList(), dto.getErrorList(), info);
        } else if (methodLower.startsWith(KeyWordConstants.DELETE)) {
            ParsedDeleteDto parse = new DeleteParser(methodLower, props).parse();
            return QueryBuilder.buildDeleteResult(parse.getParsedDeletes(), parse.getErrors(), info);
        } else if (methodLower.startsWith(KeyWordConstants.COUNT)) {
            //deal with it. all are just copy with find that is ok.
            ParsedCountDto parse = new CountParser(methodLower, props).parse();
            return QueryBuilder.buildCountResult(parse.getParsedCounts(), parse.getErrors(),info);
        }

        return null;
    }
}
