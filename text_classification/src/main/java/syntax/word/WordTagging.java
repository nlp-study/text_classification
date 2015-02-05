package syntax.word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import util.FileRead;
import base.CodePrase;

public class WordTagging {
	public List<SegTerm> excute(String input)
	{
		List<Term> parses = ToAnalysis.parse(input);
		
		List<SegTerm> terms = new ArrayList<SegTerm>();
		for(Term parse:parses)
		{
			SegTerm segTerm = new SegTerm(parse.getRealName(),parse.getNatureStr());
			terms.add(segTerm);
		}
		
		return terms;
	}
	
	public List<List<SegTerm>> excut(List<String> inputs)
	{
		List<List<SegTerm>> sentenceTerms = new ArrayList<List<SegTerm>>();
		
		for(int i=0;i<inputs.size();++i)
		{
			List<SegTerm> temp = excute(inputs.get(i));
			sentenceTerms.add(temp);
		}
		return sentenceTerms;
	}
	
	public static List<String> segForWord(String input)
	{
		List<Term> parses = BaseAnalysis.parse(input);
		
		List<String> terms = new ArrayList<String>();
		for(Term parse:parses)
		{			
			terms.add(parse.getRealName());
		}
		
		return terms;
	}
	
	public static void main(String[] args) throws IOException
	{   
		String file = "C:/projectStudy/data/text_classify/data/answer/C31-Enviornment/C31-Enviornment0756.txt";
		String sourceFile = FileRead.readFile(file);
		sourceFile = CodePrase.full2HalfChange(sourceFile);
		List<String> words = WordTagging.segForWord(sourceFile);
		
		System.out.println(words);
	}

}
