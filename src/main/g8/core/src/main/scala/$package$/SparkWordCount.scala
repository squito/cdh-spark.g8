package $package$


import java.io.File

import scala.collection.Map

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

object SparkWordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("simple-word-count").setMaster(args(0))

    // just b/c the snappy libs require some setup :(
    // TODO fix this
    conf.set("spark.broadcast.compress", "false")
    conf.set("spark.shuffle.compress", "false")
    conf.set("spark.shuffle.spill.compress", "false")

    val sc = new SparkContext(conf)

    val inputFile = if (args.length > 1) args(1) else "pom.xml"


    println(s"loading data from : ${new File(inputFile).getAbsolutePath}")

    val lineCount = sc.accumulator(0L)
    val wordCount = sc.accumulator(0L)
    val textLines: RDD[String] = sc.textFile(inputFile)
    val words: RDD[String] = textLines.flatMap { line =>
      lineCount += 1
      val w = line.replaceAll("\\W", " ").split("\\W")
      wordCount += w.length.toLong
      w
    }

    println("first try at printing accumulators: 0 because all transformations so far are lazy")
    println(s"lineCount = ${lineCount.value}")
    println(s"wordCount = ${wordCount.value}")

    val wordCountPairs: RDD[(String, Long)] = words.map{word => (word, 1L)}.reduceByKey{_ + _}
    wordCountPairs.cache()

    println("second try at printing accumulators: still all 0")
    println(s"lineCount = ${lineCount.value}")
    println(s"wordCount = ${wordCount.value}")

    wordCountPairs.cache()

    // very simple histogram -- we could do better
    val countHistogram: Map[Long, Long] = wordCountPairs.map{ case( word, counts) => (counts, 1L)}
      .reduceByKey{_ + _}.collectAsMap


    println("third try at printing accumulators: success!")
    println(s"lineCount = ${lineCount.value}")
    println(s"wordCount = ${wordCount.value}")

    println("word count histogram:")
    countHistogram.toIndexedSeq.sortBy{x => -x._1}.zipWithIndex.foreach{println}


    // sort the counts, figure out how deep we have to go to get 100
    var totalWords = 0L
    val minCount =  countHistogram.toIndexedSeq.sortBy{x => -x._1}
      .takeWhile{ case (counts, words) =>
          totalWords += words
          totalWords < 100
      }.last._1
    println(s"minCount = $minCount")

    val topWords = wordCountPairs.filter{ case ( word, counts) => counts >= minCount}.collect()
    topWords.sortBy{x => -x._2}.zipWithIndex
      .foreach{ case ((word, counts), idx) => println(s"""$idx. "$word" $counts""")}
  }
}
