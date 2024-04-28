import express from "express";
import bodyParser from "body-parser";
import qr from "qr-image";
import fs from "fs";

const app = express();
const port = 3000;

app.use(express.static("public"));
app.use(bodyParser.urlencoded({ extended: true }));

app.set("view engine", "ejs");

app.get("/", (req, res) => {
    res.render("index", { url: null });
});

app.post("/submit", (req, res) => {
    var url = req.body["url"];
    var qr_png = qr.imageSync(url, { type: "png" });
    fs.writeFileSync("public/imgs/qr_img.png", qr_png);

    fs.writeFile("public/URL.txt", url, (err) => {
        if (err) throw err;
        console.log("The file has been saved!");
    });

    res.render("index", { url: url });
});

app.get("/download", (req, res) => {
    const file = "public/imgs/qr_img.png";
    res.download(file);
});


app.listen(port, () => {
    console.log(`Listening on port ${port}.`);
});
