import React from 'react';
import { QRCodeCanvas  } from 'qrcode.react';

function Ticket({ ticket, event }) {
    return (
        <div className="ticket">
            <div className="ticket-header">
                <img src={`/imgs/${event?.banner || 'default-banner.jpg'}`} alt={event?.title || 'Event'}style={{width: '200px', height: '100px', marginBottom: '10px'}}/>
                <div className="ticket-info">
                    <h3 className="ticket-title">{event?.title || 'Unknown Event'}</h3>
                    <h4 className="ticket-category">{ticket.ticketCategory}</h4>
                    <p className="ticket-name">{ticket.fullName}</p>
                </div>
            </div>
            <QRCodeCanvas value={ticket.qrCode} size={128} className="ticket-qr" />
        </div>
    );
}

export default Ticket;
