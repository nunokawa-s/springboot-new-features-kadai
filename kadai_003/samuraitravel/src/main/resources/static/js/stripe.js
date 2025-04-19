const stripe = Stripe('pk_test_51R0hwaC0wQ7qLK5XAQG41HREfRKVkMSgmn7p3kHa5xY2RFdzyQRI32WbrchWsUiy1Cfka7xGeLwTItWYlQvEABoP00qrT0VJUS');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});


