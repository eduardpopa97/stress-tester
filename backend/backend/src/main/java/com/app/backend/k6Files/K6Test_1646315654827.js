import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '4', target: 1}
		{ duration: '2', target: 4}
 	],
};

export default function () {
 const BASE_URL = 'google.com'

	const responses = http.batch([
		['GET', '${BASE_URL}', null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}