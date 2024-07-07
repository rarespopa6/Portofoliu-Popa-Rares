import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';
import { propTypes } from 'react-bootstrap/esm/Image';

function PaymentForm(props) {
  const stripe = useStripe();
  const elements = useElements();

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!stripe || !elements) {
      // Stripe.js nu a fost încă încărcat.
      return;
    }

    const cardElement = elements.getElement(CardElement);

    const { error, paymentMethod } = await stripe.createPaymentMethod({
      type: 'card',
      card: cardElement,
    });

    if (error) {
      console.error('Eroare la crearea metodei de plată:', error);
    } else {
      console.log('Metoda de plată creată cu succes:', paymentMethod);
      // trimite paymentMethod.id la server pentru a finaliza plata
    }
  };

  return (
    <div className="card-payment-form">
        <form onSubmit={handleSubmit}>
        <CardElement />
        <button type="submit" disabled={!stripe}>
            Pay {props.amount} Lei
        </button>
        </form>
    </div>
  );
}

export default PaymentForm;
