import http from 'k6/http'
import { sleep } from 'k6'

export const options = {
	stages: [
		{ duration: '', target: },
 	],
};

export default function () {
 const BASE_URL = '43wwe'

	const responses = http.batch([
		['GET', '${BASE_URL}', null, {tags: { name: 'localtest' } }]
	]);

	sleep(1);

}