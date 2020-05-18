package sample.model;

import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONWorkspace {
    ArrayList<Group> groups = new ArrayList<Group>();

    public JSONWorkspace(){}

    static public ArrayList<Group> fromFile(File file) throws Exception {
        JSONWorkspace jsonWorkspace = new JSONWorkspace();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String json = "";
        json = br.readLine();

        ObjectMapper mapper = new ObjectMapper();
        jsonWorkspace = mapper.readValue(json, JSONWorkspace.class);

        return jsonWorkspace.getGroups();
    }

    public void save(File f) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(this);

        PrintWriter writer = new PrintWriter(f, StandardCharsets.UTF_8);
        writer.write(s);
        writer.flush();
        writer.close();

    }

    public void loadGroups(ObservableList<Group> grps) {
        groups.clear();
        groups.addAll(grps);
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
}
