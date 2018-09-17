package cn.momosv.blog.weka;

import cn.momosv.blog.weka.model.House;
import cn.momosv.blog.weka.util.WekaUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils;
import weka.core.converters.LibSVMSaver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WekaApplicationTests {

    public static final String WEKA_PATH = "arff/";
    public static final String WEATHER_NOMINAL_PATH = "data/weka/weather.nominal.arff";
    public static final String WEATHER_NUMERIC_PATH = "data/weka/weather.numeric.arff";
    public static final String SEGMENT_CHALLENGE_PATH = "data/weka/segment-challenge.arff";
    public static final String SEGMENT_TEST_PATH = "data/weka/segment-test.arff";
    public static final String IONOSPHERE_PATH = "data/weka/ionosphere.arff";

    public static void pln(String str) {
        System.out.println(str);
    }

    @Test
    public void testLinearRegression() throws Exception {


        List<House> houseList = new ArrayList<>();
        houseList.add( new House(205000,3529,9191,6,0,0));
        houseList.add(   new House(224900,3247,10061,5,1,1));
        houseList.add(     new House( 197900,4032,10150,5,0,1));
        houseList.add(     new House( 189900,2397,14156,4,1,0));
        houseList.add(    new House( 195000,2200,9600,4,0,1));
        houseList.add(     new House( 325000,3536,19994,6,1,1));
        houseList.add(    new House(230000,2983,9365,5,0,1));

        Instances houseset =    WekaUtil.generatePopularInstance(houseList,House.class);



        Instances dataset = ConverterUtils.DataSource.read(WEKA_PATH + "houses.arff");
        dataset.setClassIndex(dataset.numAttributes()-5);
        LinearRegression linearRegression = new LinearRegression();
        try {
            linearRegression.buildClassifier(dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double[] coef = linearRegression.coefficients();
        // 评估线性回归的结果
        Evaluation eval = new Evaluation(dataset);
        eval.evaluateModel(linearRegression, dataset);// 评估结果
        // 构造结果对象
        StringBuilder sb = new StringBuilder();
        sb.append("机器学习后产生的线性回归公式:\n" + linearRegression.toString() + "\n\n");
        sb.append("评估此结果:" + eval.toSummaryString() + "\n");
        System.out.println(sb.toString());


        //%3198,9669,5,3,1,219328  3529,9191,6,0,0,205000
        double myHouseValue = (coef[1] * 3198) +
                (coef[2] * 9669) +
                (coef[3] * 5) +
                (coef[4] * 3) +
                (coef[5] * 1) +
                coef[6];
/*        double myHouseValue =
                        (coef[0] * 3529) +
                        (coef[1] * 9191)+
                        (coef[2] * 6) +
                        (coef[3] * 0) +
                        (coef[4] * 0) +
                         coef[6];*/

        System.out.println(myHouseValue);
    }

    @Test
    public void testRandomForestClassifier() throws Exception {
        List<House> houseList = new ArrayList<>();
        houseList.add( new House(205000,3529,9191,6,0,0));
        houseList.add(   new House(224900,3247,10061,5,1,1));
        houseList.add(     new House( 197900,4032,10150,5,0,1));
        houseList.add(     new House( 189900,2397,14156,4,1,0));
        houseList.add(    new House( 195000,2200,9600,4,0,1));
        houseList.add(     new House( 325000,3536,19994,6,1,1));
        houseList.add(    new House(230000,2983,9365,5,0,1));

      //  Instances instances =    WekaUtil.generatePopularInstance(houseList,House.class);
       Instances instances = ConverterUtils.DataSource.read(WEKA_PATH + "houses.arff");
        instances.setClassIndex(0);
        System.out.println(instances);
        System.out.println("------------");
        RandomForest rf = new RandomForest();

        rf.buildClassifier(instances);

        System.out.println(rf.toString());

        // 保存模型
        SerializationHelper.write("LibSVM.model", rf);//参数一为模型保存文件，classifier4为要保存的模型

        double sum = instances.numInstances(), right = 0.0f;//测试语料实例数right = 0.0f;
        for(int  i = 0;i<sum;i++)//测试分类结果  1
        {
            System.out.println(rf.classifyInstance(instances.instance(i)));
            System.out.println(instances.instance(i).classValue());
            System.out.println("---------##########------");
            if(rf.classifyInstance(instances.instance(i))/instances.instance(i).classValue()>0.9)//如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
            {
                right++;//正确值加1
            }
        }
        Classifier svm =  new LibSVM();
        svm.buildClassifier(instances);
        double  sright = 0.0f;//测试语料实例数right = 0.0f;
        for(int  i = 0;i<sum;i++)//测试分类结果  1
        {
            System.out.println(svm.classifyInstance(instances.instance(i)));
            System.out.println(instances.instance(i).classValue());
            System.out.println(svm.classifyInstance(instances.instance(i))/instances.instance(i).classValue());
            System.out.println("---------##########------");
            if(svm.classifyInstance(instances.instance(i))/instances.instance(i).classValue()>0.9)//如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
            {
                sright++;//正确值加1
            }
        }


      /*  // 获取上面保存的模型
        Classifier classifier8 = (Classifier) weka.core.SerializationHelper.read("LibSVM.model");
        double right2 = 0.0f;
        for(int  i = 0;i<sum;i++)//测试分类结果  2 (通过)
        {
            System.out.println(classifier8.classifyInstance(instances.instance(i)));
            System.out.println(instances.instance(i).classValue());
            System.out.println("---------##########------");
            if(classifier8.classifyInstance(instances.instance(i))==instances.instance(i).classValue())//如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
            {
                right2++;//正确值加1
            }
        }*/
        System.out.println(right);
        System.out.println(sright);
        System.out.println(sum);
        System.out.println("RandomForest classification precision:"+(right/sum));


    }

    @Test
    public void contextLoads() {
    }

}
