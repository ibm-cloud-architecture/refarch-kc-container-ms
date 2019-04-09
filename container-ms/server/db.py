#!/usr/bin/python2.4
import psycopg2
try:
    conn=psycopg2.connect(
        host = "bd2d0216-0b7d-4575-8c0b-d2e934843e41.6131b73286f34215871dfad7254b4f7d.databases.appdomain.cloud",
        port = "31384",
        dbname = "ibmclouddb",
        user = "ibm_cloud_c9587d97_28f1_4da3_9254_dd56907ef40c",
        password = "2d1c5269de3ea3766a5a9329ef874bdc077e2e57a336a3ba2a4d95dad7b91fa3"
    )
    print ("Connected to the database")
except:
    print ("Unable to connect to the database")

city = 'Oakland'

cur = conn.cursor()
#cur.execute("CREATE TABLE CONTAINERS(ID VARVARCHAR (64) NOT NULL,LATITUDE DOUBLE PRECISION,LONGITUDE DOUBLE PRECISION,TYPE  VARCHAR (35) ,STATUS VARCHAR (35),CURRENT_CITY VARCHAR (35),BRAND VARCHAR (35),CAPACITY DOUBLE PRECISION, PRIMARY KEY (ID));")
cur.execute("INSERT INTO CONTAINERS VALUES (%s, %s, %s,%s,%s,%s,%s,%s)", (2, 20, city))
#cur.execute("DROP TABLE CUSTOMERS;")
cur.execute("SELECT * FROM containers;")
data=cur.fetchall()
print(data)
conn.commit()
cur.close()
conn.close()
