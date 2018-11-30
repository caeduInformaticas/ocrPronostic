package com.aasana.caedu.ocrprognostic.controller.persistencia;

public class PlainText {

    public static final String ROOT_DIR = "./";
    public static final String TXT_EXTENSION = "txt";
    public static final String CSV_EXTENSION = "csv";
    public static final String COMA_SEPARATOR = ",";

    private String separator;

    public PlainText(String tableName) {
        this(tableName, CSV_EXTENSION, COMA_SEPARATOR);
    }

    public PlainText(
            String tableName, String extension, String separator) {
       // super(ROOT_DIR + tableName + "." + extension);
        this.separator = separator;
    }

   /* @Override
    public void writeRegistries(List<Field> fields, List<Registry> registries)
            throws InconsistentPersistenceException {
        File targetFile = new File(getPath());
        List<String> fieldNames = fields
                .stream()
                .map((Field field) -> field.getName())
                .collect(Collectors.toList());
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            try (BufferedWriter writer
                         = new BufferedWriter(new FileWriter(targetFile))) {
                writer.write(fieldNames2CSV(fieldNames));
                writer.newLine();
                for (Registry registry : registries) {
                    writer.write(registry2CSV(registry));
                    writer.newLine();
                }
                writer.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void writeRegistry(Registry registry)
            throws InconsistentPersistenceException {
        File targetFile = new File(getPath());
        if (!targetFile.exists()) {
            throw new InconsistentPersistenceException(getPath());
        }
        try (BufferedWriter writer
                     = new BufferedWriter(new FileWriter(targetFile, true))) {
            writer.write(registry2CSV(registry));
            writer.newLine();
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Registry> readRegistries(List<Field> fields)
            throws InconsistentPersistenceException, ValidationException {

        File sourceFile = new File(getPath());
        if (sourceFile.exists()) {
            return readRegistries(sourceFile, fields);
        } else {
            createPersistenceFile(sourceFile, fields);
            return new ArrayList<>();
        }
    }

    private List<Registry> readRegistries(File sourceFile, List<Field> fields)
            throws InconsistentPersistenceException, ValidationException {

        try (BufferedReader reader
                     = new BufferedReader(new FileReader(sourceFile))) {
            checkFieldsMatch(reader.readLine(), fields);
            List<Registry> registry = new ArrayList<>(100);
            String line = reader.readLine();
            while (line != null) {
                registry.add(createRegistryFromLine(line, fields));
                line = reader.readLine();
            }
            return registry;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void createPersistenceFile(File targetFile, List<Field> fields) {

        List<String> fieldNames = fields
                .stream()
                .map((Field field) -> field.getName())
                .collect(Collectors.toList());
        try {
            targetFile.createNewFile();
            try (BufferedWriter writer
                         = new BufferedWriter(new FileWriter(targetFile))) {
                writer.write(fieldNames2CSV(fieldNames));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void checkFieldsMatch(String firstLine, List<Field> fields)
            throws InconsistentPersistenceException {

        if (firstLine == null) {
            throw new InconsistentPersistenceException(getPath());
        }
        String[] readFields = splitString(firstLine);
        if (readFields.length == fields.size()) {
            for (int index = 0; index < readFields.length; index++) {
                String readFiled = readFields[index];
                Field field = fields.get(index);
                if (!field.getName().equals(readFiled)) {
                    throw new InconsistentPersistenceException(getPath());
                }
            }
        } else {
            throw new InconsistentPersistenceException(getPath());
        }
    }

    private Registry createRegistryFromLine(String line, List<Field> fields)
            throws InconsistentPersistenceException, ValidationException {

        String[] values = splitString(line);
        if (fields.size() != values.length) {
            throw new InconsistentPersistenceException(getPath());
        }
        Registry registry = new Registry(fields.size());
        for (int index = 0; index < fields.size(); index++) {
            String value = values[index];
            Field field = fields.get(index);
            if (!field.accepts(value)) {
                throw new ValidationException(
                        value, field.getName(), field.getTypeName());
            }
            registry.set(index, value);
        }
        return registry;
    }

    private String fieldNames2CSV(List<String> fieldNames) {
        StringBuilder stringBuilder = new StringBuilder(fieldNames.size() * 10);
        for (String fieldName : fieldNames) {
            stringBuilder.append(fieldName);
            stringBuilder.append(separator);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private String registry2CSV(Registry registry) {
        StringBuilder stringBuilder = new StringBuilder(registry.getSize() * 10);
        for (Object value : registry.data()) {
            stringBuilder.append(String.valueOf(value));
            stringBuilder.append(separator);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private String[] splitString(String string) {
        return string.split(separator, -1);
    }
*/
}
