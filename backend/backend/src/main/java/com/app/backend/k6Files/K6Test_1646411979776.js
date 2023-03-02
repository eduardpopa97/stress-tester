import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '3s', target: 2},
 	],
};

export default function () {
 const BASE_URL = 'http://test-api.k6.io/public/crocodiles/2/?format=api';

	const responses = http.batch([
		['GET', '${BASE_URL}', null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}