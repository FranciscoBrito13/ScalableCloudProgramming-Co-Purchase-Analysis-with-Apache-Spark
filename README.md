# ScalableCloudProgramming-Co-Purchase-Analysis-with-Apache-Spark
This project was developed as part of the Scalable and Cloud Programming course and implements a distributed system to perform co-purchase analysis using Scala and Apache Spark.

The objective is to process a dataset of supermarket orders and calculate how often each pair of products appears together in the same order. This analysis is widely used in recommendation systems, retail analytics, and market basket analysis.

##Dataset
The dataset consists of pairs of integers in the format:

order_id,product_id

Each row indicates that the product product_id was purchased in the order order_id. For orders with multiple products, there are multiple rows with the same order_id.

Example:
1,12
1,14
2,8
2,12
2,14

##Objective
The program identifies all pairs of products that appear together in at least one order and counts how many times each pair co-occurs. The output is saved as a CSV file in the format:

product1,product2,count
Only one entry is saved per pair, e.g. 12,14,2 implies that products 12 and 14 appeared together in 2 different orders.

##Technologies

Scala
Apache Spark (RDD API)
Google Cloud Dataproc
Google Cloud Storage (GCS)

##How to Run on Google Cloud Dataproc
Create a GCS bucket and upload:

The dataset file (e.g. order_products.csv)

The compiled JAR file (e.g. scp_project_scala_2.12-0.1.0-SNAPSHOT.jar)

Create a cluster (example with 2 workers):

gcloud dataproc clusters create cluster-2w \
  --region=europe-west1 \
  --num-workers=2 \
  --worker-machine-type=n2-standard-2 \
  --worker-boot-disk-size=240 \
  --master-machine-type=n2-standard-2 \
  --master-boot-disk-size=240 \
  --image-version=2.2-debian12 \
  --project=YOUR_PROJECT_ID
Submit the Spark job:

bash
Copy
Edit
gcloud dataproc jobs submit spark \
  --cluster=cluster-2w \
  --region=europe-west1 \
  --class=Main \
  --jars=gs://YOUR_BUCKET_NAME/THE_JAR_NAME.jar
Output will be saved in your GCS bucket, as defined in the code (val outputPath).

