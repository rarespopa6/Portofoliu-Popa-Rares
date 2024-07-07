import React from "react";
import InfoCard from "./InfoCard/InfoCard";

function Info(){
    return (<div id="info">
        <InfoCard title="Buy smart" subtitle="Enjoy our varied gamma of items." emoji="💎" />
        <InfoCard title="Fast shipping" subtitle="We ship in the whole country. Comfortable and reliable." emoji="⚡️" />
        <InfoCard title="30 days Return" subtitle="You can change your mind within 30 days." emoji="🔙" />
    </div>);
}

export default Info;