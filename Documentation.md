### 1. write ddl based on ER graph

#### 1.1 Entity
* **Staff**: `gender` can be _'MALE'_ or _'FEMALE'_
* **City**
* **Company**
* **Ship**
* **Item**
* **Container**

#### 1.2 Weak Entity
* **Tax_Info**: weak entity of `City`, contains import and export tax rate for each type of item
* **Verification**: weak entity of `Staff`, containing their login password and authority level.
The attribute `Authority` could be _'COMPANY_MANAGER'_, _'COURIER'_, _'SEAPORT_OFFICER'_ or _'SUSTC_MANAGER'_
* **Ship_State**: weak entity of `Ship`. `State` can be _'SAILING'_ or _'DOCKING'_
* **Item_Type**: the type of `Item`. Only contains one column: `Type`
* **Item_State**: the current state of the `Item`. `State` can be 
_'PICKING_UP'_, _'TO_EXPORT_TRANSPORTING'_, _'EXPORT_CHECKING'_, _'EXPORT_CHECK_FAILED'_,
_'PACKING_TO_CONTAINER'_, _'WAITING_FOR_SHIPPING'_, _'SHIPPING'_, _'UNPACKING_FROM_CONTAINER'_, 
_'IMPORT_CHECKING'_, _'IMPORT_CHECK_FAILED'_, _'FROM_IMPORT_TRANSPORTING'_, _'DELIVERING'_, _'FINISH'_

#### 1.3 Relationship
* **Ship_Container**: what `Container` that `Ship` is transporting
* **Container_Item**: what `Item` is in `Container`
* **Staff_City**: working `City` of `Staff`
* **Staff_Company**: working `Company` of `Staff`
* **Courier_Item**: what `Item` is being transported by `Staff`
* **Item_Route**: the `City` that `Item` will pass.
`Stage` of the transportation can be _'IMPORT'_, _'EXPORT'_, _'DELIVERY'_ or _'RETRIEVAL'_.


### 2. add functions and triggers to the tables
* trigger of the table `Logger`: when an item's status is changed, log the change (optional)


### 3. finish implementation of the interfaces
* class `Authority`, which will automatically check if the user and its password is correct.
Also act as a factory to create `User` objects.
* separately implement the interfaces(`CompanyManager`, `Courier`, `SeaportOfficer`, `SustcManager`), 
to better test their functions. All seperated implemented classes are inherited from `User`. 

#### 3.1 DatabaseManipulation
* **Constructor**: In this step a new database and four users will be created.
Those users correspond to the four authorities. 
(Since it is required that the root user cannot be used in later operations)

#### 3.2 SustcManager
* **getCompanyCount()**: Query `Company`
* **getCityCount()**: Query `City`
* **getCourierCount()**: Query `Courier`
* **getShipCount()**: Query `Ship`
* **getItemInfo(String name)**: Query name in `Item` for item detailed information. 
Query its id in `Transportation_Info` for the item's transportation information
* **getShipInfo(String name)**: Query name in `Ship` for its detailed information. 
Query its id in `Ship_Port_At` for its status.
If the query return null, that means the ship is sailing
* **getContainerInfo(String code)**: Query code in `Container` for its detailed information. 
Query the code in `Container_Item` for its status.
* **getStaffInfo(String name)**: Query name in `Staff` for id and detailed information
Query its id in `Verification` for its password and authority.
Query its id in `Staff_Company` for its company, and get the name in `Company`
Query its id in `Staff_City` for its city, and get the name in `City`

#### 3.3 Courier
* **newItem(ItemInfo item)**: Add the item to `Item`, 
then set `State` in `Item_State` to _'PICKING_UP'_.
Add the other information to `Transportation_Info`
* **setItemState(String name, ItemState s)**: 
  * Query in `Transportation_Info` to see if able to change the state(same city)
  * Update 'Item_State':
    * _'PICKING_UP'_ : set to _'TO_EXPORT_TRANSPORTING'_
    * _'TO_EXPORT_TRANSPORTING'_ : set to _'EXPORT_CHECKING'_
    * _'From_IMPORT_TRANSPORTING'_ : set to _'From_IMPORT_TRANSPORTING'_, 
    and add the Courier to `Transportation_Info` of type _'DELIVERY'_
    * _'From_IMPORT_TRANSPORTING'_ : set to _'DELIVERING'_
    * _'DELIVERING'_ : set to _'FINISH'_

#### 3.4 CompanyManager
* **getImportTaxRate(String city, String itemClass)**: 
Query `City` to get the city id, then query `Tax_Info` for the import tax rate
* **getExportTaxRate(String city, String itemClass)**:
Query `City` to get the city id, then query `Tax_Info` for the export tax rate
* **loadItemToContainer(String itemName, String containerCode)**:
  * Query the name in `Item_State` to see if is _'EXPORT_CHECKING'_
  * Query the code `Container_Item` to see if it has packaged an item.
  * Add `Item` and `Container` to `Container_Item`.
* **loadContainerToShip(String shipName, String containerCode)**:
  * Query the code in `Container_Item`, get the item name.
  * Query `Item_State` to see if is _'PACKING_TO_CONTAINER'_.
  * Query the code in `Ship_Container` to ensure the container is not loaded to another ship.
  * Query the name in `Ship` get its id. Query `Ship_State` to see if state is _'DOCKING'_.
  * Add the relation to `Ship_Container`, then update `Item_State` to _'WAITING_FOR_SHIPPING'_
* **shipStartSailing(String shipName)
  * Query the name in `Ship` get its id. 
  * Query `Ship_State` to see if state is _'DOCKING'_.
  * Update `Ship_State` to _'SAILING'_
  * Query `Ship_Container` to get all the containers in the ship.
  * Query `Container_Item` to get all the items in the containers.
  * Update `Item_State` to _'SHIPPING'_ for all the items
* **unloadItem(LogInfo log, String item)**
  * Query the name in `Item_State` to see if is _'SHIPPING'_
  * Get the container code and delete the relation in `Container_Item`
  * Delete the relation in `Ship_Container`
  * Update `Ship_State` to _'DOCKING'_
  * Update `Item_State` to _'UNPACKING_FROM_CONTAINER'_
* **itemWaitForChecking(LogInfo log, String item)**:
`Item_State` from _'UNPACKING_FROM_CONTAINER'_ to _'IMPORT_CHECKING'_

#### 3.5 SeaportOfficer
* **getAllItemsAtPort()**:
  * Query `Staff_City` to get the city id.
  * Query `Transportation_Info` to get all the items in the city.
    * For items from _'EXPORT'_, query `Item_State` to see if is 
    _'EXPORT_CHECKING'_, _'PACKING_TO_CONTAINER'_ or _'WAITING_FOR_SHIPPING'_
    * For items from _'IMPORT'_, query `Item_State` to see if is 
    _'UNPACKING_FROM_CONTAINER'_ or _'IMPORT_CHECKING'_
* **setItemCheckState(LogInfo log, String itemName, bool success)**
  * Query the name in `Item_State` to see if is _'EXPORT_CHECKING'_ or _'IMPORT_CHECKING'_
  * if (!success) Update `Item_State` to _'EXPORT_CHECK_FAILED'_ or _'IMPORT_CHECK_FAILED'_