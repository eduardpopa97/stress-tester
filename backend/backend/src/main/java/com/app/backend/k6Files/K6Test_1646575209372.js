import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '2s', target: 5},
 	],
};

export default function () {
 const BASE_URL = 'https://test-api.k6.io/public/crocodiles/';

	const responses = http.batch([
		['GET', '${BASE_URL}', null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}