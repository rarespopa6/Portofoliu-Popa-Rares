import React from "react";

function InfoCard(props){
    return (
        <div className="info-card">
            <p className="info-title">{ props.title }</p>
            <p className="info-subtitle">{ props.subtitle }</p>
            <p className="info-emoji">{ props.emoji }</p>
        </div>
    );
}

export default InfoCard;