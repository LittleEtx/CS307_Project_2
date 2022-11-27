package com.littleetx.cs307_project_2.interfaces;

public interface IDatabaseManipulation extends ISustcManager, ICourier, ICompanyManager, ISeaportOfficer {
	// Your implemented class shall have constructor like:
	//		Constructor(String database, String root, String pass)
	// See project description for more details.

	void $import(String recordsCSV, String staffsCSV);
}
