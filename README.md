PROJECT
Billing Data for supermarket

SET UP
BillingGenerator is the driving class for the billing process
BillingService is the interface for validation and billing calculation
Item is th POJO for holding item data
ItemRepo is the repo for ref data
CsvReader is the implementation used to retrieve ref data

INPUT
Input is of the form <ItemCode>.<Qty>
Example input is "A.9 W.8 O.7" - This means 9 apples, 8 melons. The code for product is in the ref data
Input can be staggered like billing counter - "A.7 A.1 W.8 O.9 A.1"

REF DATA
Ref Data store is csv. Data is stored in format <ItemCode>,<Item>,<Price>,<Deal>
Deal is of the format <Items for>/<Items priced>. 2/1 means 2 for 1


