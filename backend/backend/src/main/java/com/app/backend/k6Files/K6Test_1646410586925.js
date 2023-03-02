import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '2', target: 5},
 	],
};

export default function () {
 const BASE_URL = 'https://test-api.k6.io/public/crocodiles/2'

	const responses = http.batch([
		['GET', '${BASE_URL}', null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}