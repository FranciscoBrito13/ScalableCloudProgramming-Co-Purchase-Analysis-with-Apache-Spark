# ScalableCloudProgramming-Co-Purchase-Analysis-with-Apache-Spark
This project was developed as part of the Scalable and Cloud Programming course and implements a distributed system to perform co-purchase analysis using Scala and Apache Spark.

The objective is to process a dataset of supermarket orders and calculate how often each pair of products appears together in the same order.

# Dataset

The dataset consists of pairs of integers
Each row indicates that the product product_id was purchased in the order order_id. For orders with multiple products, there are multiple rows with the same order_id.

# Objective
The program identifies all pairs of products that appear together in at least one order and counts how many times each pair co-occurs. The output is saved as a CSV file in the format:

product1,product2,count
Only one entry is saved per pair, e.g. 12,14,2 implies that products 12 and 14 appeared together in 2 different orders.

# How to Run on Google Cloud Dataproc

Example Configuration Based on This Project
Input CSV file: gs://co-purchase/order_products.csv
Output directory: gs://co-purchase/final_output
JAR file to submit: scp_project_scala_2.12-0.1.0-SNAPSHOT.jar
Main class: Main


How to Run on Google Cloud Dataproc

Upload the CSV dataset and the compiled JAR to your GCS bucket.

Create the cluster with: gcloud dataproc clusters create cluster-2w
--region=europe-west1
--num-workers=2
--worker-machine-type=n2-standard-2
--worker-boot-disk-size=240
--master-machine-type=n2-standard-2
--master-boot-disk-size=240
--image-version=2.2-debian12
--project=YOUR_PROJECT_ID

Submit the job with: gcloud dataproc jobs submit spark
--cluster=cluster-2w
--region=europe-west1
--class=Main
--jars=gs://co-purchase/scp_project_scala_2.12-0.1.0-SNAPSHOT.jar

Output

The output will be saved as a CSV in: gs://co-purchase/final_output/
