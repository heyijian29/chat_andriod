package sdk.chat.core.dao;

import android.content.Context;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ben on 4/13/18.
 */

public class DatabaseUpgradeHelper extends DaoMaster.OpenHelper {

    // Legacy column names
    public final static Property LastMessageId = new Property(9, Long.class, "lastMessageId", false, "LAST_MESSAGE_ID");


    public DatabaseUpgradeHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

        List<Migration> migrations = getMigrations();

        // Only run migrations past the old version
        for (Migration migration : migrations) {
            if (oldVersion < migration.getVersion()) {
                migration.runMigration(db);
            }
        }
    }

    private List<Migration> getMigrations() {
        List<Migration> migrations = new ArrayList<>();
        migrations.add(new MigrationV2());
        migrations.add(new MigrationV3());
        migrations.add(new MigrationV4());
        migrations.add(new MigrationV6());
        migrations.add(new MigrationV7());
        migrations.add(new MigrationV8());
        migrations.add(new MigrationV9());
        migrations.add(new MigrationV10());
        migrations.add(new MigrationV11());
        migrations.add(new MigrationV12());
        migrations.add(new MigrationV13());
        migrations.add(new MigrationV14());
        migrations.add(new MigrationV15());
        migrations.add(new MigrationV16());
        migrations.add(new MigrationV17());
        migrations.add(new MigrationV18());
        migrations.add(new MigrationV19());

        // Sorting just to be safe, in case other people add migrations in the wrong order.
        Comparator<Migration> migrationComparator = (m1, m2) -> m1.getVersion().compareTo(m2.getVersion());
        Collections.sort(migrations, migrationComparator);

        return migrations;
    }

    private static class MigrationV2 implements Migration {

        @Override
        public Integer getVersion() {
            return 2;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            //UserDao.createTable(db, false);
            db.execSQL("ALTER TABLE " + MessageDao.TABLENAME + " ADD COLUMN " + MessageDao.Properties.NextMessageId.columnName + " LONG");
            db.execSQL("ALTER TABLE " + MessageDao.TABLENAME + " ADD COLUMN " + LastMessageId.columnName + " LONG");
        }
    }

    private static class MigrationV3 implements Migration {

        @Override
        public Integer getVersion() {
            return 3;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table

            ThreadMetaValueDao.createTable(db, true);
            UserMetaValueDao.createTable(db, true);
            db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " DROP COLUMN " + "META_DATA");
            db.execSQL("ALTER TABLE " + MessageDao.TABLENAME + " DROP COLUMN " + "RESOURCES");

        }
    }

    private static class MigrationV4 implements Migration {

        @Override
        public Integer getVersion() {
            return 4;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " DROP COLUMN " + "LAST_UPDATED");
        }
    }

    private static class MigrationV6 implements Migration {

        @Override
        public Integer getVersion() {
            return 6;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            MessageMetaValueDao.createTable(db, true);

            ReadReceiptUserLinkDao.dropTable(db, false);
            ReadReceiptUserLinkDao.createTable(db, true);

            // Clear down the text database and re synchronize
            MessageDao.dropTable(db, false);
            MessageDao.createTable(db, true);
        }
    }

    private static class MigrationV7 implements Migration {

        @Override
        public Integer getVersion() {
            return 7;
        }

        @Override
        public void runMigration(Database db) {
            ReadReceiptUserLinkDao.dropTable(db, false);
            ReadReceiptUserLinkDao.createTable(db, true);
        }
    }

    private static class MigrationV8 implements Migration {
        @Override
        public Integer getVersion() {
            return 8;
        }

        @Override
        public void runMigration(Database db) {
            ThreadDao.dropTable(db, true);
            ThreadDao.createTable(db, true);
        }
    }

    private static class MigrationV9 implements Migration {
        @Override
        public Integer getVersion() {
            return 9;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + ThreadDao.TABLENAME + " DROP COLUMN " + "LAST_MESSAGE_ID");
        }
    }

    private static class MigrationV10 implements Migration {
        @Override
        public Integer getVersion() {
            return 10;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + MessageDao.TABLENAME + " ADD COLUMN " + MessageDao.Properties.PreviousMessageId.columnName + " LONG");
        }
    }

    private static class MigrationV11 implements Migration {
        @Override
        public Integer getVersion() {
            return 11;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " ADD COLUMN " + UserDao.Properties.IsOnline.columnName + " BOOLEAN");
        }
    }

    private static class MigrationV12 implements Migration {
        @Override
        public Integer getVersion() {
            return 12;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + ThreadDao.TABLENAME + " ADD COLUMN " + ThreadDao.Properties.CanDeleteMessagesFrom.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + ThreadDao.TABLENAME + " ADD COLUMN " + ThreadDao.Properties.LoadMessagesFrom.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + ThreadDao.TABLENAME + " ADD COLUMN " + ThreadDao.Properties.Draft.columnName + " TEXT");
        }
    }

    private static class MigrationV13 implements Migration {
        @Override
        public Integer getVersion() {
            return 13;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + ThreadMetaValueDao.TABLENAME + " ADD COLUMN " + ThreadMetaValueDao.Properties.BooleanValue.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + ThreadMetaValueDao.TABLENAME + " ADD COLUMN " + ThreadMetaValueDao.Properties.IntegerValue.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + ThreadMetaValueDao.TABLENAME + " ADD COLUMN " + ThreadMetaValueDao.Properties.LongValue.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + ThreadMetaValueDao.TABLENAME + " ADD COLUMN " + ThreadMetaValueDao.Properties.FloatValue.columnName + " INTEGER");
        }
    }

    private static class MigrationV14 implements Migration {
        @Override
        public Integer getVersion() {
            return 14;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + ThreadMetaValueDao.TABLENAME + " DROP COLUMN " + "VALUE" + " INTEGER");
            db.execSQL("ALTER TABLE " + ThreadMetaValueDao.TABLENAME + " ADD COLUMN " + ThreadMetaValueDao.Properties.StringValue.columnName + " INTEGER");
        }
    }

    private static class MigrationV15 implements Migration {
        @Override
        public Integer getVersion() {
            return 15;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + ThreadDao.TABLENAME + " DROP COLUMN " + "IMAGE_URL");
            db.execSQL("ALTER TABLE " + ThreadDao.TABLENAME + " DROP COLUMN " + "NAME");
        }
    }

    private static class MigrationV16 implements Migration {
        @Override
        public Integer getVersion() {
            return 16;
        }

        @Override
        public void runMigration(Database db) {
            PublicKeyDao.createTable(db, true);
        }
    }
    private interface Migration {
        Integer getVersion();
        void runMigration(Database db);
    }

    private static class MigrationV17 implements Migration {
        @Override
        public Integer getVersion() {
            return 17;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + ThreadDao.TABLENAME + " ADD COLUMN " + ThreadDao.Properties.UserAccountID.columnName + " TEXT");
        }
    }

    private static class MigrationV18 implements Migration {
        @Override
        public Integer getVersion() {
            return 18;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + ThreadDao.TABLENAME + " RENAME TO old_table");
            ThreadDao.createTable(db, true);
            db.execSQL("INSERT INTO " + ThreadDao.TABLENAME + " SELECT * FROM old_table");
            db.execSQL("DROP TABLE old_table");
        }
    }

    private static class MigrationV19 implements Migration {
        @Override
        public Integer getVersion() {
            return 19;
        }

        @Override
        public void runMigration(Database db) {
            db.execSQL("ALTER TABLE " + MessageDao.TABLENAME + " ADD COLUMN " + MessageDao.Properties.EncryptedText.columnName + " TEXT");
        }
    }

}
