
import org.apache.spark.{SparkConf, SparkContext}
import java.text.SimpleDateFormat

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
case class customer(custid: Int,name:String,street: Int,city:String,state:String,zip:Int)
case class sale(timestamp: Long,custid:Int,salesprice:Float)


object joiningOfTwoFiles extends App {
  Logger.getLogger("org").setLevel(Level.OFF)


  val conf = new SparkConf()

  conf.setAppName("joiningOfTwoFiles")
  conf.setMaster("local");
  val sc = new SparkContext(conf)
  val rdd1 = sc.textFile("/home/knoldus/Desktop/file1.txt").map(_.split("#"))
  val rdd2 = sc.textFile("/home/knoldus/Desktop/file2.txt").map(_.split("#"))
  val custrecord1 = rdd1.map(x =>(x(0).toInt,customer(x(0).toInt,x(1),x(2).toInt,x(3),x(4),x(5).toInt)))
  val custrecord2 = rdd2.map(x =>(x(1).toInt,sale(x(0).toLong,x(1).toInt,x(2).toFloat)))
  val resultcust_record2 = sc.parallelize(custrecord2.collect)
  val joinedresult = custrecord1.join(resultcust_record2)
  val result = joinedresult.map(x=>(x._2._1,x._2._2))
 /* val resultcontainingtimestamp = result.map(x=>(x._2._1,x._2._2._1))*/
  result.collect.foreach(println)

 def convertingTimestamptoDate(timestamp: Long): String ={


   val ts = timestamp * 1000L
   val df = new SimpleDateFormat("yyyy-MM-dd")
   val date = df.format(ts)
   date
 }



}

