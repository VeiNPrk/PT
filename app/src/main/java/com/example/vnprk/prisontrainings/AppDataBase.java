package com.example.vnprk.prisontrainings;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by VNPrk on 03.03.2017.
 */
@Database(name = AppDataBase.NAME, version = AppDataBase.VERSION)
public class AppDataBase {
        public static final String NAME = "AppDatabase";
        public static final int VERSION = 1;
        //public static final int VERSION = 2;
/*
        @Migration(version=2, priority = 2, database = AppDataBase.class)
        public static class AlterMigration2 extends AlterTableMigration<NoteClass>{

            public AlterMigration2(Class<NoteClass> table) {
                super(table);
            }
			
			@Override
			public void onPreMigrate() {
				addColumn(SQLiteType.INTEGER, "dateCreate");
                addColumn(SQLiteType.INTEGER, "dateUpdate");
			}
        }*/
        /*
   @Migration(version = 2, priority = 1, database = AppDataBase.class)
    public static class UpdateMigration2 extends UpdateTableMigration<NoteClass> {

        public UpdateMigration2(Class<NoteClass> table) {
            super(table);
        }
		@Override
		public void onPreMigrate() {
			set(NoteClass_Table.dateCreate.eq(new Date()));
            set(NoteClass_Table.dateUpdate.eq(new Date()));
		}
    }*/
}

