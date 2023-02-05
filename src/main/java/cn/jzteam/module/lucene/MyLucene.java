package cn.jzteam.module.lucene;

import cn.jzteam.core.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class MyLucene {

    public final static String INDEX_DIR = "/Users/oker/Downloads/test/lucene_index";

    public final static String TEST_PATH = "/Users/oker/Downloads/test/lucene_docs";

    //创建索引
    public static void testCreateIndex() throws IOException {
        // 指定索引库的存放位置Directory对象
        Directory directory = FSDirectory.open(new File(INDEX_DIR).toPath());
        // 索引库还可以存放到内存中
        // Directory directory = new RAMDirectory();

        // 指定一个标准分析器，对文档内容进行分析
        Analyzer analyzer = new StandardAnalyzer();

        // 创建indexwriterCofig对象，参数：分析器对象
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // 创建一个indexwriter对象：指定了 索引库位置 和 分析器
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // 原始文档的路径
        File file = new File(TEST_PATH);
        File[] fileList = file.listFiles();
        for (File file2 : fileList) {

            //创建文件名域：分别指定 域的名称、域的内容、是否存储
            Field fileNameField = new TextField("fileName", file2.getName(), Field.Store.YES);

            //文件大小域
            Field fileSizeField = new LongPoint("fileSize", FileUtils.sizeOf(file2));

            //文件路径域（不分析、不索引、只存储）
            Field filePathField = new StoredField("filePath", file2.getPath());

            //文件内容域
            Field fileContentField = new TextField("fileContent", FileUtils.readFileToString(file2), Field.Store.YES);

            //创建field对象，将field添加到document对象中
            Document document = new Document();
            document.add(fileNameField);
            document.add(fileSizeField);
            document.add(filePathField);
            document.add(fileContentField);

            //使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库。
            indexWriter.addDocument(document);
        }

        //关闭IndexWriter对象。
        indexWriter.close();
    }

    public static void main(String[] args) throws Exception {
        //testCreateIndex();

        //testMatchAllDocsQuery();
        testMultiFiledQueryParser();
    }


    public static void testMatchAllDocsQuery() throws Exception {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(INDEX_DIR).toPath());
        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建Indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建查询条件
        //使用MatchAllDocsQuery查询索引目录中的所有文档
        Query query = new MatchAllDocsQuery();
        //执行查询
        //第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);

        //查询结果的总条数
        System.out.println("查询结果的总条数：" + topDocs.totalHits);
        System.out.println("----------------------------------");
        System.out.println();
        //遍历查询结果
        //topDocs.scoreDocs存储了document对象的id
        //ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //scoreDoc.doc属性就是document对象的id
            //int doc = scoreDoc.doc;
            //根据document的id找到document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            //文件名称
            System.out.println(document.get("fileName"));
            //文件内容
            System.out.println(document.get("fileContent"));
            //文件大小
            System.out.println(document.get("fileSize"));
            //文件路径
            System.out.println(document.get("filePath"));
            System.out.println("----------------------------------");
        }
        //关闭indexreader对象
        indexReader.close();
    }

    public static void testMultiFiledQueryParser() throws Exception {
        //创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(new File(INDEX_DIR).toPath());
        //创建IndexReader对象，需要指定Directory对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建Indexsearcher对象，需要指定IndexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //可以指定默认搜索的域是多个
//        String[] fields = {"fileName", "fileContent"};
//        //创建一个MulitFiledQueryParser对象
//        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
//        Query query = queryParser.parse("船迟");
        // 指定一个默认域（方便parse方法不写域:）；指定一个分析器，要跟创建索引使用相同的分析器
        QueryParser queryParser = new QueryParser("fileName", new StandardAnalyzer());
        // "船迟"--会使用默认域fileName；"fileContent:船迟"--使用指定域；都会使用指定的分析器
        Query query = queryParser.parse("fileContent:我们");

        // 执行查询：第一个参数是查询对象，第二个参数是查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);

        //查询结果的总条数
        System.out.println("查询结果的总条数：" + topDocs.totalHits);
        System.out.println("----------------------------------");
        System.out.println();
        //遍历查询结果
        //topDocs.scoreDocs存储了document对象的id
        //ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //scoreDoc.doc属性就是document对象的id
            //int doc = scoreDoc.doc;
            //根据document的id找到document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            //文件名称
            System.out.println(document.get("fileName"));
            //文件内容
            System.out.println(document.get("fileContent"));
            //文件大小
            System.out.println(document.get("fileSize"));
            //文件路径
            System.out.println(document.get("filePath"));
            System.out.println("----------------------------------");
        }
        //关闭indexreader对象
        indexReader.close();
    }
}
