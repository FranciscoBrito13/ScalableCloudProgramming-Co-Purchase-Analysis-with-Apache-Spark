import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.collect_list
import org.apache.spark.sql.types._

object Main {


  def main(args: Array[String]) = {
    val start = System.nanoTime()

    val spark = SparkSession.builder()
      .appName("SPC_Project")
      .getOrCreate()


    // Defines the structure of the DataFrame
    val schema = StructType(Array(
      StructField("order_id", IntegerType, nullable = false),
      StructField("product_id", IntegerType, nullable = false)
    ))

    // Creates the DataFrame
    val df = spark.read
      .schema(schema)
      .csv("gs://co-purchase/order_products.csv")

    val outputPath = "gs://co-purchase/final_output"

    // Groups the DataFrame by order_id
    val productsByOrder = df.groupBy("order_id")
      .agg(collect_list("product_id").as("products"))

    // Generate all unique product pairs
    val productPairs = productsByOrder.rdd.flatMap(row => {
      val products = row.getAs[Seq[Int]]("products").toList // Convert to an immutable list
      products.combinations(2).map {
        case Seq(x, y) if x < y => ((x, y), 1)
        case Seq(x, y) => ((y, x), 1)
      }
    })

    val coPurchaseCounts = productPairs.reduceByKey(_ + _)

    // Creates a CSV file directly with headers
    import spark.implicits._
    val resultDF = coPurchaseCounts.map {
      case ((product1, product2), count) => (product1, product2, count)
    }.toDF("product1", "product2", "count")

    resultDF.coalesce(1).write
      .option("header", "true")
      .csv(outputPath)

    spark.stop()

    val end = System.nanoTime()
    val durationSeconds = (end - start) / 1e9d
    println(f"Execution time: $durationSeconds%.2f seconds")
  }
}
