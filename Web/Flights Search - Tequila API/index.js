import express from "express";
import axios from "axios";

const app = express();
const port = 3000;

const TEQUILA_ENDPOINT = "https://tequila-api.kiwi.com";
const TEQUILA_API_KEY = "sfx2dO2VZfdqZbS8FwTBbzIgJhpO3pnb";

app.use(express.static("public"));
app.use(express.urlencoded({ extended: true }));

var sent = false;

app.set("view engine", "ejs");

app.get("/", (req, res) => {
    res.render("index.ejs");
});

app.post("/search", async (req, res) => {
    try {
        const { departureCity, arrivalCity, departureDate, returnDate, maxStops } = req.body;

        const response = await axios.get(`${TEQUILA_ENDPOINT}/v2/search`, {
            params: {
                fly_from: departureCity,
                fly_to: arrivalCity,
                date_from: departureDate,
                partner_market: "us",
                partner: "picky",
                curr: "EUR",
                sort: "price",
                limit: 6,
                max_stopovers: parseInt(maxStops, 10)
            },
            headers: {
                apikey: TEQUILA_API_KEY,
            },
        });

        const flights1 = response.data.data.map(flight => ({
            ...flight,
            numStops: flight.route.length - 1, 
            airlineName: flight.airlineName
        }));

        const uniqueFlights1 = flights1.filter((flight, index, self) =>
            index === self.findIndex(f => (
                f.price === flight.price
            ))
        );

        const departureDay = departureDate.toString();
        const returnDay = returnDate.toString();

        const response2 = await axios.get(`${TEQUILA_ENDPOINT}/v2/search`, {
            params: {
                fly_from: arrivalCity,
                fly_to: departureCity,
                date_from: returnDate,
                partner_market: "us",
                partner: "picky",
                curr: "EUR",
                sort: "price",
                limit: 6,
                max_stopovers: parseInt(maxStops, 10)
            },
            headers: {
                apikey: TEQUILA_API_KEY,
            },
        });

        const flights2 = response2.data.data.map(flight => ({
            ...flight,
            numStops: flight.route.length - 1, 
            airlineName: flight.airlineName
        }));

        const uniqueFlights2 = flights2.filter((flight, index, self) =>
            index === self.findIndex(f => (
                f.price === flight.price
            ))
        );
        res.render("flights.ejs", { uniqueFlights1, uniqueFlights2, departureCity, arrivalCity, departureDay, returnDay });
    } catch (error) {
        console.log(error.response.data);
        res.status(500).send("Error fetching flight data");
    }
});

app.listen(port, () => {
    console.log(`Server running on port ${port}.`);
});