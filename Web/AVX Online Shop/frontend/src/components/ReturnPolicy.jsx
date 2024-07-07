import React from 'react';

function ReturnPolicy() {
  return (
    <div className="return-policy-container">
      <h2>Return Policy</h2>
      <div className="return-policy-details">
        <p>We hope you love your purchase, but if you are not completely satisfied, we offer a 30-day free return policy. Please read the details below:</p>
        <h3>Eligibility for Returns</h3>
        <ul>
          <li>Items must be returned within 30 days of receipt.</li>
          <li>Items must be in their original condition, unused, and with all tags and packaging intact.</li>
          <li>Proof of purchase is required for all returns.</li>
        </ul>
        <h3>Non-returnable Items</h3>
        <p>Certain items cannot be returned, including:</p>
        <ul>
          <li>Gift cards</li>
          <li>Personalized items</li>
          <li>Final sale items</li>
        </ul>
        <h3>How to Return</h3>
        <p>To initiate a return, please follow these steps:</p>
        <ol>
          <li>Contact our customer service team at <a href="mailto:returns@example.com">returns@avx.com</a> or call us at +4077725200.</li>
          <li>Provide your order number and details of the items you wish to return.</li>
          <li>Our team will provide you with a return shipping label.</li>
          <li>Pack the items securely and attach the return shipping label to the package.</li>
          <li>Drop off the package at the nearest shipping location.</li>
        </ol>
        <h3>Refunds</h3>
        <p>Once we receive your returned items, we will process your refund within 5-7 business days. Refunds will be issued to the original payment method.</p>
        <h3>Order Cancellation</h3>
        <p>You may cancel an order only if it hasn't been already sent from our Headquarters. Check order status.</p>
      </div>
    </div>
  );
}

export default ReturnPolicy;
